/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// $(document).ready( 
//            function () {
//                var table = $('#tabella').DataTable({
//                     "ajax": "/getFattureIn",
//        "columns": [
//            { "data": "dataReg" },
//            { "data": "numeroReg" },
//            { "data": "dataFatt" },
//            { "data": "numeroFatt" },
//            { "data": "pIva" },
//            { "data": "denominazione" },
//            { "data": "imponibile" }
//        ]
//    } );
//} );
        
        $(document).ready( function () {
	 var table = $('#employeesTable').DataTable({
			"sAjaxSource": "/employees",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "id"},
                            { "mData": "dataReg" },
                            { "mData": "numeroReg" },
                            { "mData": "dataFatt" },
                            { "mData": "numeroFatt" },
                            { "mData": "pIva" },
                            { "mData": "denominazione" },
                            { "mData": "imponibile" }
			]
	 })
});