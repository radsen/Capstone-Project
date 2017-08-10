'use strict';

const functions = require('firebase-functions');
const request = require('request-promise');
const fs = require('fs');

const xpath = require('xpath');
const dom = require('xmldom').DOMParser;
const parser = require('xml2js');

const RUNT_URL = 'https://www.runt.com.co/consultamovil/publico/vehiculos/';
const SIMIT_URL = 'https://181.48.11.4/ServiciosSimit/EstadoCuenta?wsdl';


var Subpoena = function(){
	return {
					code : '',
    				description : '',
    				address : '',
    				status  : '',
    				date : '',
    				camera_detection : '',
    				offender : '',
    				number  : '',
    				license_plate  : '',
    				department : '',
    				vehicle_purpose : '',
    				vehicle_type : '',
    				cost : ''
    			};
}

exports.runt = functions.database.ref('/vehicles/{pushId}').onWrite(event => {

	if(!event.data.val() || event.data.previous.val()){
		console.log("Not Adding a new vehicle");
		return;
	}

	var uuid = event.auth.variable ? event.auth.variable.uid : null;

	var request_body = {
		placa: event.data.val().licensePlate,
		tdoc: event.data.val().idType,
		ndoc: event.data.val().idNumber
	}

	return request({
		uri: RUNT_URL,
		method: 'POST',
		json: true,
		headers: {
			'Content-Type' : 'application/json',
			'token' : '0f5f884f-63cc-4367-a25a-6b94591bdf15'
		},
		body: request_body,
		resolveWithFullResponse: true
	}).then(response => {
		if (response.statusCode >= 400) {
      		throw new Error(`HTTP Error: ${response.statusCode}`);
    	}

    	var vehicle = event.data.ref;
    	var root = event.data.adminRef.root;
		
		return vehicle.update(extractRegistrationData(response.body))
		.then(function(){

			var path = '/vehiclesPerUser/' + uuid + '/' + vehicle.key;

			vehicle.once("value", function(snapshot){
				var vehicle_user_ref = root.child(path);
				vehicle_user_ref.set(snapshot.val());
			});

		})
		.then(function(){
			var v_docs_ref = root.child('/vehicleDocuments/' + event.data.key);
			// Adding SOAT
			v_docs_ref.push().set(createInsurance(response.body));
			// Adding emissions
			v_docs_ref.push().set(createEmission(response.body));

		})
		.then(function(){

			//https://181.48.11.4:443/ServiciosSimit/EstadoCuenta?wsdl
			var request_body = createSOAPEnvelope(event.data.val().idType, event.data.val().idNumber);

			console.log("Before sending the request");
			request({
				uri: SIMIT_URL, 
      			headers: {
						'Content-Type' : 'text/xml;charset=utf-8',
						'Content-Length' : request_body.length,
						'SOAPAction' : ''
					},
      			method: 'POST',
      			body: request_body,
      			rejectUnauthorized: false,
      			requestCert: true,
      			agent: false
			})	
			.then(response => {
				
				var subpoena_path = '/subpoena_x_user/' + uuid;

				var doc = new dom().parseFromString(response);
				var nodes = xpath.select("//comparendos", doc);
				console.log(nodes);

				for(var i = 0; i < nodes.length; i++){
					var subpoena = new Subpoena();
					subpoena.code   			= xpath.select("string(//codigoInfraccion)", nodes[i]);
    				subpoena.description     	= xpath.select("string(//descripcionInfraccion)", nodes[i]);
    				subpoena.address 			= xpath.select("string(//direccionComparendo)", nodes[i]);
    				subpoena.status   			= xpath.select("string(//estadoComparendo)", nodes[i]);
    				subpoena.date     			= xpath.select("string(//fechaComparendo)", nodes[i]);
    				subpoena.camera_detection 	= xpath.select("string(//fotodeteccion)", nodes[i]);
    				subpoena.offender   		= xpath.select("string(//infractorComparendo)", nodes[i]);
    				subpoena.number     		= xpath.select("string(//numeroComparendo)", nodes[i]);
    				subpoena.license_plate 		= xpath.select("string(//placaVehiculo)", nodes[i]);
    				subpoena.department   		= xpath.select("string(//secretariaComparendo)", nodes[i]);
    				subpoena.vehicle_purpose    = xpath.select("string(//servicioVehiculo)", nodes[i]);
    				subpoena.vehicle_type 		= xpath.select("string(//tipoVehiculo)", nodes[i]);
    				subpoena.cost 				= xpath.select("string(//total)", nodes[i]);

					console.log(subpoena);
    				
    				root.child(subpoena_path).push().set(subpoena);
				}

			});

		});

	});

});

function createSOAPEnvelope(type, number){
	var typeAsInt = (type === 'C')? 1 : 2;
	
	var envelope = '<?xml version="1.0" encoding="UTF-8"?>' + 
	'<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns1="http://Servicios/">' +
	'<SOAP-ENV:Body>' +
	'<ns1:Comparendos>' +
	'<Documento>' + number + '</Documento>' +
	'<TipoDocumento>' + typeAsInt + '</TipoDocumento>' +
	'</ns1:Comparendos>' +
	'</SOAP-ENV:Body>' +
	'</SOAP-ENV:Envelope>';

	return envelope.toString();
}

    // "soat": {
    //     "vigente": true,
    //     "fechaExpedicion": "05/11/2016",
    //     "fechaVencimiento": "07/11/2017",
    //     "fechaInicio": "08/11/2016",
    //     "noSoat": "18692756",
    //     "valido": true
    // }

function createInsurance(val) {
	return {
				type: 'I',
				desc: Object.keys(val)[0],
				number: val.soat.noSoat,
				issued: val.soat.fechaExpedicion,
				expires: val.soat.fechaVencimiento,
				starts: val.soat.fechaInicio,
				valid: val.soat.vigente,
				approved: val.soat.valido
			}
}

// "revision": {
//         "vigente": true,
//         "noRevision": "131649113",
//         "fechaRevision": "25/07/2017",
//         "fechaVencimiento": "25/07/2018",
//         "valido": true,
//         "reqrtm": true,
//         "placa": "BST508"
//     }

function createEmission(val) {
	
	var emissionsNumber = val.revision.hasOwnProperty('noRevision') ? val.revision.noRevision: null;
	var issuedDate = val.revision.hasOwnProperty('fechaRevision') ? val.revision.noRevision: null;
	var expirationDate = val.revision.hasOwnProperty('fechaVencimiento') ? val.revision.noRevision: null;

	return {
				type: 'E',
				desc: Object.keys(val)[1],
				number: emissionsNumber,
				issued: issuedDate,
				expires: expirationDate,
				valid: val.revision.vigente,
				approved: val.revision.valido,
				required: val.revision.reqrtm
			}
}				

function extractRegistrationData(val){
	return {
		model: val.vehiculo.linea,
		regDate: val.vehiculo.fmatri,
		brand: val.vehiculo.marca 
	}
}