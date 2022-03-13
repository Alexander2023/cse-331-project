/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Map from "./Map";
import Controls from "./Controls";
import {Edge} from "./types";

interface AppState {
    pathStart: string; // building to begin a path
    pathEnd: string; // building to end a path
    path: Edge[]; // path from start to end buildings to display on map
}

/**
 * Top-level application that lets the user select a start and end building
 * and view the path between them on a map of the UW Seattle campus
 */
class App extends Component<{}, AppState> {
    constructor(props: {}) {
        super(props);

        this.state = {
            pathStart: "CS2",
            pathEnd: "CS2",
            path: []
        }
    }

    /**
     * Performs a fetch request for the path from pathStart to pathEnd
     */
    requestPath = async () => {
        try {
            let response = await fetch(`http://localhost:4567/path?start=${this.state.pathStart}&end=${this.state.pathEnd}`);

            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }

            let json = await response.json();

            let temp: Edge[] = [];
            for (let edge of json.path) {
                temp.push({x1: edge.start.x, y1: edge.start.y, x2: edge.end.x, y2: edge.end.y});
            }

            this.setState({
                path: temp
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    /**
     * Updates state and requests a new path based on changes to the start dropdown
     */
    onStartDropdownChange = (option: string) => {
        this.setState({
            pathStart: option
        }, this.requestPath)
    }

    /**
     * Updates state and requests a new path based on changes to the end dropdown
     */
    onEndDropdownChange = (option: string) => {
        this.setState({
            pathEnd: option
        }, this.requestPath)
    }

    /**
     * Resets component to its initial state when prompted to clear
     */
    onClearPressed = () => {
        this.setState({
            pathStart: "CS2",
            pathEnd: "CS2",
            path: []
        })
    }

    render() {
        return (
            <div>
                <Map path={this.state.path}/>
                <Controls
                    pathStart={this.state.pathStart}
                    pathEnd={this.state.pathEnd}
                    onStartDropdownChange={this.onStartDropdownChange}
                    onEndDropdownChange={this.onEndDropdownChange}
                    onClearPressed={this.onClearPressed}
                />
            </div>
        );
    }
}

export default App;
