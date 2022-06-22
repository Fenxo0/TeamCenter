import React, {Component} from 'react';
import {
    Route,
    withRouter,
    Switch
} from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';

import './App.css';

import Login from '../model/auth/login/Login';
import AppHeader from "../common/header/AppHeader";
import Content from "../common/content/Content";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'
import Footer from "../common/footer/Footer";
import PDFViewer from './AllPages';
import AllPages from './AllPages';

toast.configure()

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false
        }
        
        this.handleLogin = this.handleLogin.bind(this);
    }


    test() {
        console.log("OK");
    }


    handleLogin(redirectTo = "/") {
        toast.success(
            "Вход выполнен!",
            {position: toast.POSITION.BOTTOM_LEFT}
        )
        this.props.history.push("/");
    }

    render() {
        const notify = () => {

        }
        return (
            <div className="app">
                <AppHeader isAuthenticated={this.state.isAuthenticated}
                           currentUser={this.state.currentUser}
                           onLogout={this.handleLogout}/>

                <div className="auth-wrapper">
                    <div className="auth-inner">
                        <Switch>
                            <Route exact path='/' component={Content}/>
                            <Route path="/intstruction/:pdfName"
                                   render={
                                       (props) =>
                                           <AllPages {...props} />
                                   }/>
                            <Route path="/login"
                                   render={
                                       (props) =>
                                           <Login onLogin={this.handleLogin} {...props} />
                                   }/>
                        </Switch>
                    </div>
                </div>
                <Footer/>
            </div>
        );
    }
}


export default withRouter(App);
