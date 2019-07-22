var ajaxUrl = "ajax/meals/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();

    $('#meals-filter').on('submit', function (e) {
        e.preventDefault();
        updateTableWithFilter();
    });

    $('#meals-filter-reset').on('click', function (e) {
        e.preventDefault();
        $('#meals-filter').trigger('reset');
        updateTable();
    });
});

function updateTableWithFilter() {
    $.get(ajaxUrl + "filter", $('#meals-filter').serialize()).done(function (data) {
        fillTable(data);
    });
}