import { Component } from "react";
import { Alert, Button } from "react-bootstrap";
import { executeSavedQueries } from "../../util/APIUtils";
import React, { useState } from 'react';
import {
    Accordion,
    AccordionItem,
    AccordionItemHeading,
    AccordionItemButton,
    AccordionItemPanel,
} from 'react-accessible-accordion';

import 'react-accessible-accordion/dist/fancy-example.css';

class ItemList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            items: [],
            users: [],
            groups: [],
            showModal: false,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            open: true,
            currentVotes: [],
            isLoading: false
        };
        this.runHolder = this.runHolder;
        this.loadItemList = this.loadItemList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.close = this.close.bind(this);
        this.open = this.open.bind(this);
    }

    close(){
        this.setState({ showModal: false });
    }

    open(){
        this.setState({ showModal: true });
    }

    handleClick() {
        this.setState(prevState => ({
          isToggleOn: !prevState.isToggleOn
        }));
        var panel = this.nextElementSibling;
        if (panel.style.display === "block") {
          panel.style.display = "none";
        } else {
          panel.style.display = "block";
        }
      }

    async loadItemList() {
        debugger
        if (this.props.searchOptions) {
            console.log(this.props.searchOptions)
            await executeSavedQueries(this.props.searchOptions)
                .then(response => {
                    this.setState({
                        items: new Map(Object.entries(response.objects)),
                        users: response.users,
                        groups: response.groups,
                        isLoading: false
                    })
                    debugger
                    console.log(this.state.items)
                });
        }

        //this.setState({
        //    isLoading: true
        //});

        //this.setState({
        //    items: promise.object,
        //    isLoading: false
        //})
        //promise
        //    .then(response => {
        //        console.log(response.content)
        //        const items = this.state.items.slice();
        //        this.setState({
        //            items: response.content,
        //            isLoading: false
        //        })
        //    }).catch(error => {
        //    this.setState({
        //        isLoading: false
        //    })
        //});
    }

    handleLoadMore() {
        this.loadItemList(this.state.page + 1);
    }

    async componentDidUpdate(nextProps) {
        //debugger
        if (localStorage.getItem("Session") === null) {
            // Reset State
            this.setState({
                items: [],
                users: [],
                groups: [],
                showModal: false,
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });
            return
        }
        if (this.state.items.length === 0) {
            await this.loadItemList();
            console.log(this.state.items)
        }
    }

    render() {
        let array = [], values = [], valuesUsers =[], valuesGroups = [], count = 0, showModal = false
        const {users, groups} = this.state
        this.state.items.forEach(function(value, key) {
            value.split(';').forEach(v => values.push(v))
            users[count].split(';').forEach(u => valuesUsers.push(u))
            groups[count].split(';').forEach(g => valuesGroups.push(g))
            if (key === "DEMO-965-Распределительное устройство ПОС") {
                array.push(
                    <AccordionItem>
                    <AccordionItemHeading>
                        <AccordionItemButton>
                            {key}
                        </AccordionItemButton>
                    </AccordionItemHeading>
                    <AccordionItemPanel>
                        {values[0]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                    <AccordionItem>
                        <AccordionItemButton>
                            {values[1]}
                        </AccordionItemButton>
                        <AccordionItemPanel>
                            {valuesGroups[0]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesGroups[1]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesGroups[2]}
                        </AccordionItemPanel>
                        </AccordionItem>
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {values[2]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        <AccordionItem>
                        <AccordionItemButton>
                            {values[3]}
                        </AccordionItemButton>
                        <AccordionItemPanel>
                            {valuesUsers[0]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[1]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[2]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[3]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[4]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[5]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[6]}
                        </AccordionItemPanel>
                        <AccordionItemPanel>
                            {valuesUsers[7]}
                        </AccordionItemPanel>
                        </AccordionItem>
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        <d>Инструкции к данной детали - </d>
                        <Button to="route" target="_blank" onClick={() => {window.open("http://localhost:9090/intstruction/Chertezh.pdf")}} > Чертеж.pdf </Button>
                        <d>         </d>
                        <Button to="route" target="_blank" onClick={() => {window.open("http://localhost:9090/intstruction/PSE-A-75-00-01-00R-720A-A.pdf")}} > PSE-A-75-00-01-00R-720A-A.pdf </Button>
                        </AccordionItemPanel>    
                </AccordionItem>,
               )
            } else {
            array.push(
                <AccordionItem>
                <AccordionItemHeading>
                    <AccordionItemButton>
                        {key}
                    </AccordionItemButton>
                </AccordionItemHeading>
                <AccordionItemPanel>
                    {values[0]}
                </AccordionItemPanel>
                <AccordionItemPanel>
                <AccordionItem>
                    <AccordionItemButton>
                        {values[1]}
                    </AccordionItemButton>
                    <AccordionItemPanel>
                        {valuesGroups[0]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesGroups[1]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesGroups[2]}
                    </AccordionItemPanel>
                    </AccordionItem>
                </AccordionItemPanel>
                <AccordionItemPanel>
                    {values[2]}
                </AccordionItemPanel>
                <AccordionItemPanel>
                    <AccordionItem>
                    <AccordionItemButton>
                        {values[3]}
                    </AccordionItemButton>
                    <AccordionItemPanel>
                        {valuesUsers[0]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[1]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[2]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[3]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[4]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[5]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[6]}
                    </AccordionItemPanel>
                    <AccordionItemPanel>
                        {valuesUsers[7]}
                    </AccordionItemPanel>
                    </AccordionItem>
                </AccordionItemPanel>  
            </AccordionItem>,
           )}
           values.length = 0
           if (count !== 11) count++
        })
        return (
            <div>
                {
                !this.state.isLoading && this.state.items.length === 0 ? (
                    <div className="no-items-found">
                        <Alert variant="success">
                            <Alert.Heading>Детали</Alert.Heading>
                            <p>
                                У вас нет ни одной детали
                            </p>
                        </Alert>
                    </div>
                ) : <Accordion allowMultipleExpanded preExpanded={[0]}>
                    {array}
                </Accordion>
            }
            </div>
        )
    }
}

export default ItemList;