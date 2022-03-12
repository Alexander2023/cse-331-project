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
import CampusMap from "./Map";
import Dropdown from "./Dropdown";

interface Point {
    x: number;
    y: number;
}

interface Segment {
    start: Point;
    end: Point;
}

interface AppState {
    buildings: Map<string, string>;
    pathStart: string; // building to begin a path
    pathEnd: string; // building to end a path
    path: Segment[]; // path from pathStart to pathEnd to display on map
}

class App extends Component<{}, AppState> {
    constructor(props: {}) {
        super(props);

        this.state = {
            buildings: new Map<string, string>(),
            pathStart: "CSE",
            pathEnd: "CS2",
            path: []
        }

        this.requestBuildings();
    }

    requestBuildings = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildings");

            if (!response.ok) {
                console.log("Wrong status!");
                return;
            }

            let json = await response.json();

            let temp: Map<string, string> = new Map<string, string>();
            for (let shortName in json) { // +=+ change
                temp.set(shortName, json[shortName]);
            }

            console.log(temp);

            this.setState({
                buildings: temp
            })
        } catch (e) {
            console.log(e);
        }
    }

    requestPath = async () => {
        try {
            let response = await fetch(`http://localhost:4567/path?start=${this.state.pathStart}&end=${this.state.pathEnd}`);

            if (!response.ok) {
                console.log("Wrong status!");
                return;
            }

            let json = await response.json();

            let temp: Segment[] = [];
            for (let segment of json.path) {
                temp.push({start: segment.start, end: segment.end});
            }

            this.setState({
                path: temp
            })
        } catch (e) {
            console.log(e);
        }
    }

    onStartDropdownChange = (option: string) => {
        this.setState({
            pathStart: option
        })
    }

    onEndDropdownChange = (option: string) => {
        this.setState({
            pathEnd: option
        })
    }

    render() {
        return (
            <div>
                <CampusMap edges={this.state.path}/>
                <Dropdown
                    buildings={this.state.buildings}
                    label={"Start:"}
                    defaultOption={this.state.pathStart}
                    onDropdownChange={this.onStartDropdownChange}
                />
                <Dropdown
                    buildings={this.state.buildings}
                    label={"End:"}
                    defaultOption={this.state.pathEnd}
                    onDropdownChange={this.onEndDropdownChange}
                />
                <button onClick={() => {this.requestPath()}}>Directions</button>
            </div>
        );
    }
}

export default App;
