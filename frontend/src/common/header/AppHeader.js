import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './AppHeader.css';
import {Nav, Navbar} from "react-bootstrap";
import logo from "../../img/logo.png"

class AppHeader extends Component {
    constructor(props) {
        super(props);
    }

    handleMainClick(){
        this.props.history.push("/");
    }

    render() {
        let menuItems;
        if(localStorage.getItem('Name')) {
            menuItems = [
                <Nav.Link as={Link} to="/login" >
                    {localStorage.getItem('Name')}
                </Nav.Link>,
            ];
        } else {
            menuItems = [
                <Nav.Link as={Link} to="/login">
                    Вход
                </Nav.Link>,
            ];
        }
        return (
            <header className="app-header">
                <Navbar>
                    <Navbar.Brand href='/'>
                        <img
                            src={logo}
                            width="113"
                            height="40"
                            className='d-inline-block align-top'
                            alt='Site logo'
                        />
                    </Navbar.Brand>
                    <Navbar.Collapse>
                        <Nav className="mr-auto">

                        </Nav>
                        <Nav>
                            {menuItems}
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            </header>
        );
    }
}


export default withRouter(AppHeader);
