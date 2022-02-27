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

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import {Edge} from "./types";

interface AppState {
    text: string;
    edges: Edge[];
}

const NUM_EDGE_FIELDS: number = 5; // +=+ Should constants be defined here?
const MIN_COORD: number = 0;
const MAX_COORD: number = 4000;

class App extends Component<{}, AppState> { // <- {} means no props.
  constructor(props: any) {
    super(props);
    this.state = {
        text: "",
        edges: []
    };
  }

  onHandleInputChange = (value: string) => {
      this.setState({text: value});
  }

  onHandleDrawPressed = () => {
      const edges: Edge[] = [];

      const lines: string[] = this.state.text.split("\n");

      for (let line of lines) {
          const properties: string[] = line.split(" "); // +=+ Should we support any number of spaces?

          if (properties.length !== NUM_EDGE_FIELDS) {
              alert("Please enter each edge in the form: x1 y1 x2 y2 COLOR");
              return;
          }

          const x1: number = parseFloat(properties[0]); // +=+ How would we handle partial floats?
          const y1: number = parseFloat(properties[1]);
          const x2: number = parseFloat(properties[2]);
          const y2: number = parseFloat(properties[3]);
          const color: string = properties[4];

          const isInBounds = (val: number) => { // +=+ Is this ok style?
              return val >= MIN_COORD && val <= MAX_COORD;
          }

          if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2) ||
              !isInBounds(x1) || !isInBounds(y1) || !isInBounds(x2) || !isInBounds(y2)) {
              alert(`Please ensure x and y coordinates are floats between ${MIN_COORD} and ${MAX_COORD}`);
              return;
          }

          edges.push({x1: x1, y1: y1, x2: x2, y2: y2, color: color, key: edges.length});
      }

      this.setState({edges});
  }

  onHandleClearPressed = () => {
      this.setState({edges: []})
  }

  render() {
    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          <Map edges={this.state.edges}/>
        </div>
        <EdgeList
          onChange={this.onHandleInputChange}
          onDrawPressed={this.onHandleDrawPressed}
          onClearPressed={this.onHandleClearPressed}
          value={this.state.text}
        />
      </div>
    );
  }
}

export default App;
