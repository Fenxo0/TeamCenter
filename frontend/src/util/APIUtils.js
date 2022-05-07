import {API_BASE_URL} from '../constants';


let request = (options, contentType={'Content-Type': 'application/json'}) => {

    const headers = new Headers(contentType)

    if(localStorage.getItem("Session")) {
        headers.append('Set-Cookie', localStorage.getItem("Session"))
    }
    headers.append('Access-Control-Allow-Origin', '*')
    headers.append('X-Requested-With', 'XMLHttpRequest')

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then((response) =>  
            response.text().then(text => {
            debugger
            localStorage.setItem("Session", response.headers.get("Session").match('[^=]*$'))
            if(!response.ok) {
                return Promise.reject(text);
            }
            return text;
        })
    );
};

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + '/login',
        method: 'POST',
        //mode: 'no-cors',
        body: JSON.stringify(loginRequest)
    });
}