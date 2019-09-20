/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready( function () {
	 var table = $('#tabella').DataTable({
			"sAjaxSource": "/getFattureIn",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "Id"},
                            { "mData": "Data Reg." },
                            { "mData": "N.Reg." },
                            { "mData": "Data" },
                            { "mData": "Numero" },
                            { "mData": "P.IVA" },
                            { "mData": "Denominazione" },
                            { "mData": "Imponibile" }
			]
	 })
});