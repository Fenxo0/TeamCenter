import React, {Component} from "react";
import './Footer.css'
import {Container} from "react-bootstrap";

class Footer extends Component {
    render() {
        return (
            <Container className={"footer"} fluid>
                Footer
                <Container className={"text"} fluid>
                    <h5>Front-end TeamCenter, 2022</h5>
                    <h7>Студент группы БИМ-181 - </h7>
                    <h7>Сухочев М.Ю.</h7>
                </Container>
            </Container>
        )
    }
}

export default Footer