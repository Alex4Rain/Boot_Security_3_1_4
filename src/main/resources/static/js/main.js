
// 1. Required data to start
const requestURL = ["http://localhost:8080/api/currentUser", "http://localhost:8080/api/users", "http://localhost:8080/api/roles", "http://localhost:8080/api/admin/user"];
const tableColumnsName = ["ID", "Name", "Last Name", "Age", "E-mail", "Password", "Roles"];
const tableColumnsNameExtension = ["ID", "Name", "Last Name", "Age", "E-mail", "Password", "Roles", "Edit", "Delete"];
const userBody = {
    id: null,
    firstName: "",
    lastName: "",
    age: "",
    email: "",
    password: "",
    authorities: [{
        id: null,
        name: "",
        users: null,
        authority: ""}],
    enabled: true,
    username: "",
    accountNonLocked: true,
    accountNonExpired: true,
    credentialsNonExpired: true
};

// 2. Function to get/send data from server
function sendRequest (method, url, body = null) {
    const headers = {
        "Content-Type": "application/json"
    };
    const initWithBody = {
        method: method,
        body: JSON.stringify(body),
        headers: headers
    }
    function getInit(body) {
        if (body != null) {
            return initWithBody;
        } else {
            return {};
        }
    }
    return fetch(url, getInit(body))
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(error => {
                    const e = new Error("Oops! Something went wrong :(");
                    e.data = error;
                    throw e;
                })
            }
        });
}

// 3. Functions to handle data from server to JS

function rolesSetToString(rolesArray) {
    let array = [];
    for (let role of rolesArray) {
        array.push(role.name)
    }
    return array.join("; ")
}

function rolesStringToSet(rolesString) {
    let nameArray = rolesString.split("; ");
    let rolesArray = [];
    for (let name of nameArray) {
        if (name != null)
            rolesArray.push({
                id: null,
                name: name,
                users: null,
                authority: name
            });
    }
    return rolesArray;
}

function userToArray(user) {
    let array = [];
    array.push(user.id);
    array.push(user.firstName);
    array.push(user.lastName);
    array.push(user.age);
    array.push(user.email);
    array.push(user.password);
    array.push(rolesSetToString(user.authorities));
    return array;
}

function getUserMultiArray(userArray) {
    let userMultiArray = [];
    for (let user of userArray) {
        userMultiArray.push(userToArray(user));
    }
    return userMultiArray;
}

function getLoggedUserFromResponse(response) {
    const loggedUserArray = [];
    loggedUserArray.push(userToArray(response));
    return loggedUserArray;
}

function getUsersFromResponse(response) {
    if (response === null) {
        return [[]];
    } else {
        return getUserMultiArray(response);
    }
}

function getRolesFromResponse(response) {
    if (response === null) {
        return [{
            id: null,
            name: "",
            users: null,
            authority: ""}];
    } else {
        return response;
    }
}

//4. Functions to create elements

function createTableFromResponse(jsonData, toArrayFunction, columnsName = {}, elementId = "") {
    const userArray = toArrayFunction(jsonData);

    // Create a dynamic table
    let table = document.createElement("table");
    table.id = elementId + "_table";
    table.className = "table table-hover";
    let tr = table.insertRow(-1);

    // Create table header
    for (let i = 0; i < columnsName.length; i++) {
        let th = document.createElement("th");
        th.innerHTML = columnsName[i];
        tr.appendChild(th);
    }

    //Create roes of table
    for (let j = 0; j < userArray.length; j++) {
        tr = table.insertRow(-1);
        if (columnsName === tableColumnsNameExtension) {
            for (let i = 0; i < userArray[j].length; i++) {
                let tabCell = tr.insertCell(-1);
                tabCell.innerHTML = userArray[j][i];
            }
            let tabCellEd = tr.insertCell(-1);
            let buttonEdit = document.createElement("button");
            buttonEdit.type = "button";
            buttonEdit.innerHTML = "Edit";
            buttonEdit.className = "btn btn-info btn-white";
            buttonEdit.dataset.toggle = "modal";
            buttonEdit.dataset.target = "#editUser";
            buttonEdit.value = userArray[j][0];
            buttonEdit.addEventListener("click", function() { openModalWindow(userBody, j, "editUser");});
            tabCellEd.appendChild(buttonEdit);
            let tabCellDel = tr.insertCell(-1);
            let buttonDel = document.createElement("button");
            buttonDel.type = "button";
            buttonDel.innerText = "Delete";
            buttonDel.className = "btn btn-danger btn-white";
            buttonDel.dataset.toggle = "modal";
            buttonDel.dataset.target = "#deleteUser";
            buttonDel.value = userArray[j][0];
            buttonDel.addEventListener("click", function() { openModalWindow(userBody, j, "deleteUser");});
            tabCellDel.appendChild(buttonDel);
        } else {
            for (let data of userArray[j]) {
                let tabCell = tr.insertCell(-1);
                tabCell.innerHTML = data;
            }
        }
    }

    //Add table to HTML
    let divContainer = document.getElementById(elementId);
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
    return jsonData;
}

function putRolesInSelect(response, ident) {
    let rolesSet = getRolesFromResponse(response);
    let select = document.getElementById(ident + "_rolesSelect")
    for (let role of rolesSet) {
        let option = document.createElement("option");
        option.textContent = role.name;
        option.value = role.name;
        select.appendChild(option);
    }
    return response;
}

function doSubmitForm() {
    let buttonNew = document.getElementById("newUser_submit");
    buttonNew.addEventListener("click", submitNewUser);
    let buttonEdit = document.getElementById("editUser_submit");
    buttonEdit.addEventListener("click", () => {new Promise(submitEditUser)
        .then(() => document.getElementById("editUser_close").click())});
    let buttonDelete = document.getElementById("deleteUser_submit");

    buttonDelete.addEventListener("click", () => new Promise(submitDeleteUser).then(data => document.getElementById("deleteUser_close").click()));

}

function openModalWindow(userBody, rowNumber, ident) {
    putUserInInput(getUserFromTable(userBody, rowNumber), ident);
}

function putUserNameInNavbar(response) {

    let logUser = getLoggedUserFromResponse(response);
    document.getElementById("userName_navbar").innerHTML = logUser[0][4];
    document.getElementById("userRoles_navbar").innerHTML = logUser[0][6];
    if (!logUser[0][6].includes("ROLE_ADMIN")) {
        document.getElementById("userPanel").style.display = "block";
        document.getElementById("addUser").style.display = "none";
        document.getElementById("adminSwitch").style.display = "none";
        document.getElementById("userSwitch").className = "nav-link active"
        document.getElementById("adminPanel").style.display = "none";
    } else {
        document.getElementById("userPanel").style.display = "none";
        document.getElementById("addUser").style.display = "none";
        document.getElementById("adminSwitch").className = "nav-link active"

    }
    return response;
}

// 5. Functions to put / pull data from userBody

function getUserFromTable(userBody, rowNumber) {
    let table = document.getElementById("usersFromDb_table");
    let row = table.rows.item(rowNumber + 1);
    userBody.firstName = row.cells.item(1).innerHTML;
    userBody.lastName = row.cells.item(2).innerHTML;
    userBody.age = row.cells.item(3).innerHTML;
    userBody.email = row.cells.item(4).innerHTML;
    userBody.password = row.cells.item(5).innerHTML;
    userBody.id = row.cells.item(0).innerHTML;
    userBody.username = userBody.email;
    userBody.authorities = rolesStringToSet(row.cells.item(6).innerHTML);
    return userBody;
}

function putValueInBody(userBody, ident) {
    let rolesArray = [];
    userBody.firstName = document.getElementById(ident + "_firstName").value;
    document.getElementById(ident + "_firstName").value = "";
    userBody.lastName = document.getElementById(ident + "_lastName").value;
    document.getElementById(ident + "_lastName").value = "";
    userBody.age = document.getElementById(ident + "_age").value;
    document.getElementById(ident + "_age").value = "";
    userBody.email = document.getElementById(ident + "_email").value;
    document.getElementById(ident + "_email").value = "";
    userBody.password = document.getElementById(ident + "_password").value;
    document.getElementById(ident + "_password").value = "";
    userBody.username = userBody.email;
    if (ident !== "newUser") {
        userBody.id = document.getElementById(ident + "_id").value;
        document.getElementById(ident + "_id").value = "";
    }
    if (ident === "deleteUser") {
        rolesArray = rolesStringToSet(document.getElementById(ident + "_id").value);
    } else {
        for (let option of document.getElementById(ident + "_rolesSelect").options) {
            if (option.selected) {
                let role = {
                    id: null,
                    name: option.value,
                    users: null,
                    authority: option.value
                }
                rolesArray.push(role);
                option.selected = false;
            }
        }
    }
    userBody.authorities = rolesArray;
}

function putUserInInput(userBody, ident) {
    document.getElementById(ident + "_firstName").value = userBody.firstName;
    document.getElementById(ident + "_lastName").value = userBody.lastName;
    document.getElementById(ident + "_age").value = userBody.age;
    document.getElementById(ident + "_email").value = userBody.email;
    document.getElementById(ident + "_password").value = userBody.password;
    document.getElementById(ident + "_id").value = userBody.id;
    if (ident === "deleteUser") {
        document.getElementById(ident + "_roles").value = rolesSetToString(userBody.authorities);
    }
    clearValuesInBody(userBody);
}

function clearValuesInBody(userBody) {
    userBody = {
        id: null,
        firstName: "",
        lastName: "",
        age: "",
        email: "",
        password: "",
        authorities: [{
            id: null,
            name: "",
            users: null,
            authority: ""}],
        enabled: true,
        username: "",
        accountNonLocked: true,
        accountNonExpired: true,
        credentialsNonExpired: true
    };
}

// 6. Functions to submit data to server

function submitNewUser() {
    putValueInBody(userBody, "newUser");
//    console.log(userBody);
    sendRequest("POST", requestURL[3], userBody)
        .then(data => createTableFromResponse(data, getUsersFromResponse, tableColumnsNameExtension, "usersFromDb"))
        .then(data => document.getElementById("userTableSwitch").click())
        .catch(err => console.log(err));
    clearValuesInBody(userBody);
}

function submitEditUser() {
    putValueInBody(userBody, "editUser");
    sendRequest("PATCH", requestURL[3], userBody)
        .then(data => createTableFromResponse(data, getUsersFromResponse, tableColumnsNameExtension, "usersFromDb"))
        .catch(err => console.log(err));
    clearValuesInBody(userBody);
}

function submitDeleteUser() {
    putValueInBody(userBody, "deleteUser");
    sendRequest("DELETE", requestURL[3], userBody)
        .then(data => createTableFromResponse(data, getUsersFromResponse, tableColumnsNameExtension, "usersFromDb"))
        .catch(err => console.log(err));
    clearValuesInBody(userBody);
}

// 7. Function to setup view

function navSwitch (activeLink, activePanel, nonActiveLink, nonActivePanel) {
    document.getElementById(activeLink).className = "nav-link active"
    document.getElementById(activePanel).style.display = "block";
    document.getElementById(nonActiveLink).className = "nav-link"
    document.getElementById(nonActivePanel).style.display = "none";
    return false;
}

// 8. Main body of script

document.getElementById("adminSwitch").onclick = function () {
    return navSwitch("adminSwitch", "adminPanel", "userSwitch", "userPanel");
}
document.getElementById("userSwitch").onclick = function () {
    return navSwitch("userSwitch", "userPanel", "adminSwitch", "adminPanel");
}
document.getElementById("userTableSwitch").onclick = function () {
    return navSwitch("userTableSwitch", "showUsers", "newUserSwitch", "addUser");
}
document.getElementById("newUserSwitch").onclick = function () {
    return navSwitch("newUserSwitch", "addUser", "userTableSwitch", "showUsers");
}

sendRequest("GET",requestURL[0])
    .then(data => createTableFromResponse(data, getLoggedUserFromResponse, tableColumnsName, "loggedUser"))
    .then(data => putUserNameInNavbar(data))
    .catch(err => console.log(err));

sendRequest("GET",requestURL[1])
    .then(data => createTableFromResponse(data, getUsersFromResponse, tableColumnsNameExtension, "usersFromDb"))
    .catch(err => console.log(err));

sendRequest("GET",requestURL[2])
    .then(data => putRolesInSelect(data, "newUser"))
    .then(data => putRolesInSelect(data, "editUser"))
    .catch(err => console.log(err));

doSubmitForm();