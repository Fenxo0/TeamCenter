import {API_BASE_URL} from '../constants';


let request = (options, contentType={'Content-Type': 'application/json'}) => {

    const headers = new Headers(contentType)

    if(localStorage.getItem("Session")) {
        headers.append('Session', localStorage.getItem("Session"))
    }
    headers.append('Access-Control-Allow-Origin', '*')
    headers.append('X-Requested-With', 'XMLHttpRequest')

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then((response) =>  
            response.json().then(text => {
            localStorage.setItem("Session", response.headers.get("Session"))
            if(!response.ok) {
                return Promise.reject(text);
            }
            return text;
        })
    );
};

async function requestJSON (options) {

    let contentType={'Content-Type': 'application/json'}
    const headers = new Headers(contentType)

    const defaults = {headers: headers};
    if(localStorage.getItem("Session")) {
        headers.append('Session', localStorage.getItem("Session"))
    }
    options = Object.assign({}, defaults, options);

    return await fetch(options.url, options)
        .then(response =>
             response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        ).catch((e) => console.log((e)));
};


export function login(loginRequest) {
    return request({
        url: API_BASE_URL + '/login',
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function getAllItems() {
    return request({
        url: API_BASE_URL + '/getSavedQueries',
        method: 'GET'
    });
}

export async function executeSavedQueries(executeSavedQueriesRequest) {
    return requestJSON({
        url: API_BASE_URL + '/executeSavedQueries',
        method: 'POST',
        body: JSON.stringify(executeSavedQueriesRequest)
    });
}

export function opnePdf(pdfName) {
    return requestJSON({
        url: "http://localhost:9090" + '/instruction/' + 'pdfName',
        method: 'POST'
    });
}