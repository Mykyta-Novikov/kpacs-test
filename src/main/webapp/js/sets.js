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
            {
                id: "delete", header: [{text: "Delete", rowspan: 2}], htmlEnable: true,
                template: function () {
                    return "<button class='remove-button'>Delete</button>"
                }
            }
        ],
        height: 400,
        autoWidth: true,
        autoHeight: true,

        data: sets,

        eventHandlers: {
            onclick: {
                "remove-button": function (event, data) {
                    let id = data.row.id;
                    fetch(`./set/${id}/`, {
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

    grid.events.on("cellDblClick", function (row) {
        window.open(`./set/${row.id}/`)
    });

    let formGrid = new dhx.Grid("form_container", {
        columns: [
            {id: "id", header: [{text: "ID"}]},
            {id: "title", header: [{text: "Title"}]},
            {id: "description", header: [{text: "Description"}]},
            {id: "creationDate", header: [{text: "Creation Date"}],
                type: "date",
                dateFormat: "%d-%m-%Y"}
        ],
        height: 200,
        autoWidth: true,
        autoHeight: true,

        data: kpacs.map(parseDate),

        selection: "row",
        multiselection: true
    });

    let addForm = document.getElementById("add-form");
    addForm.onsubmit = function (event) {
        event.preventDefault();

        let formData = new FormData(addForm);
        let body = {};
        for (const [key, value] of formData) {
            body[key] = value;
        }

        body.kpacs = formGrid.selection.getCells().map(cell => cell.row.id);
        fetch("./set", {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body),
        }).then(response => {
            if (response.ok)
                response.json().then(json => grid.data.add(json));
            else
                document.location.reload()
        });
    };
};
