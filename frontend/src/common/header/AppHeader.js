import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './AppHeader.css';
import Modal from "react-bootstrap/Modal";
import {Button, Nav, Navbar} from "react-bootstrap";
import logo from "../../img/logo.png"

class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false
        }
        this.close = this.close.bind(this);
        this.open = this.open.bind(this);
    }

    handleMainClick(){
        this.props.history.push("/");
    }

    close(){
        this.setState({ showModal: false });
    }

    open(){
        this.setState({ showModal: true });
    }

    render() {
        let menuItems;
        if(localStorage.getItem('Name')) {
            menuItems = [
                <Nav.Link as={Button} onClick = {this.open}>
                    {localStorage.getItem('Name')}
                </Nav.Link>,
                <Modal show={this.state.showModal} onHide={this.close}>
                <Modal.Header closeButton>
                    <Modal.Title>Информация о пользователе</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div>Имя - {localStorage.getItem('Name')}</div>
                    <div>Группа - {localStorage.getItem('Group')}</div>
                    <div>Id - {localStorage.getItem('Id')}</div>
                    <div>Уровень лицензии - {localStorage.getItem('LicenseLevel')}</div>
                    <div>Проект - {localStorage.getItem('Project')}</div>
                    <div>Домашняя папка - {localStorage.getItem('HomeFolder')}</div>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={this.close}>Закрыть</Button>
                </Modal.Footer>
            </Modal>
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
