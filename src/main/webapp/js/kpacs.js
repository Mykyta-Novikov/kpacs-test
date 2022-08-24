"use strict";

function parseDate(responseBody) {
    let parsed = new Object(responseBody);
    parsed.creationDate = new Date(responseBody.creationDate);
    return parsed;
}

window.onload = () => {
    let grid = new dhx.Grid("container", {
        columns: [
            {id: "id", header: [{text: "ID"}, {content: "inputFilter"}]},
            {id: "title", header: [{text: "Title"}, {content: "inputFilter"}]},
            {id: "description", header: [{text: "Description"}, {content: "inputFilter"}]},
            {
                id: "creationDate",
                header: [{text: "Creation Date"}, {content: "inputFilter"}],
                type: "date",
                dateFormat: "%d-%m-%Y"
            },
            {
                id: "delete", header: [{text: "Delete", rowspan: 2}], htmlEnable: true,
                template: function () {
                    return "<button class='remove-button'>Delete</button>"
                }
            }
        ],
        height: 500,
        autoWidth: true,
        autoHeight: true,
        data: data.map(parseDate),

        eventHandlers: {
            onclick: {
                "remove-button": function (event, data) {
                    let id = data.row.id;
                    fetch(`./kpac/${id}/`, {
                        method: "delete"
                    }).then(response => {
                        if (response.status === 204)
                            grid.data.remove(id)
                        else
                            window.location.reload()
                    });
                }
            }
        }
    });

    let addForm = document.getElementById("add-form");

    addForm.onsubmit = function (event) {
        event.preventDefault();

        let formData = new FormData(addForm);
        let body = {};
        for (const [key, value] of formData) {
            body[key] = value;
        }
        fetch("./kpac", {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body),
        }).then((response) => {
            if (response.ok)
                response.json().then(json => grid.data.add(parseDate(json)));
            else
                window.location.reload();
        });
    };
};