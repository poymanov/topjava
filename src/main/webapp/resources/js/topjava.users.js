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
        var id = $(this).data("userid");
        var row = $(this).closest('tr');
        var active = !!$(this).prop('checked');
        var input = $(this);

        $.ajax({
            type: "POST",
            url: ajaxUrl + "active/" + id,
            data: {active: active},
            success: function () {
                row.toggleClass('disabled-user');
                successNoty("Updated");
            },
            error: function () {
                input.prop('checked', !active);
            }
        });
    });
});