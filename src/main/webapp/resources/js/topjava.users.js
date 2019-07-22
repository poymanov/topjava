var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
                "asc"
            ]
        ]
    });
    makeEditable();

    $('.user-activity').on('change', function (e) {
        var url;
        var id = $(this).data("userid");
        var row = $(this).closest('tr');

        if ($(this).prop('checked')) {
            url = ajaxUrl + "enable";
        } else {
            url = ajaxUrl + "disable";
        }

        url += "/" + id;

        $.ajax({
            type: "POST",
            url: url,
            data: {id: id},
            success: function () {
                row.toggleClass('disabled-user');
                successNoty("Updated");
            }
        });
    });
});