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

interface Point {
    x: number;
    y: number;
}

interface Segment {
    start: Point;
    end: Point;
}

interface AppState {
    pathStart: string; // building to begin a path
    pathEnd: string; // building to end a path
    path: Segment[]; // path from pathStart to pathEnd to display on map
}

class App extends Component<{}, AppState> {
    constructor(props: {}) {
        super(props);

        this.state = {
            pathStart: "CSE",
            pathEnd: "CS2",
            path: []
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
                <Map edges={this.state.path}/>
                <Controls
                    pathStart={this.state.pathStart}
                    pathEnd={this.state.pathEnd}
                    onStartDropdownChange={this.onStartDropdownChange}
                    onEndDropdownChange={this.onEndDropdownChange}
                    requestPath={this.requestPath}
                />
            </div>
        );
    }
}

export default App;
