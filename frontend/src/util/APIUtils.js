import {API_BASE_URL} from '../constants';


const request = (options, headers_add= {}) => {
    const headers = new Headers({});

    for (const [key, value] of Object.entries(headers_add)) {
        headers.set(key, value);
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    return new Promise(function (resolve, reject) {
        let xhr = new XMLHttpRequest();
        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.responseText);
            } else {
                reject(xhr.statusText);
            }
        };
        xhr.open('POST', options.url, true)
        xhr.setRequestHeader('SOAPAction', 'login');
        xhr.setRequestHeader('Content-Type', 'text/xml;charset=UTF-8');
        xhr.send(options.body);
    })
}


export function login(loginRequest) {
    var xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ses="http://teamcenter.com/Schemas/Core/2006-03/Session">"` +
    `<soapenv:Header/>` +
    `<soapenv:Body>` +
       `<ses:LoginInput username="${loginRequest.username}" password="${loginRequest.password}"/>` +
       `</soapenv:Body>` +
       `</soapenv:Envelope>`
    const headers = {'SOAPAction': 'login', 'Content-Type': 'text/xml;charset=UTF-8'}
    return request({
        url: API_BASE_URL,
        method: 'POST',
        body: xml
    }, headers);
}