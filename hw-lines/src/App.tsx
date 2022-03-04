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
import {NUM_EDGE_PROPERTIES, UW_MAX_COORD, UW_MIN_COORD} from "./Constants";

interface AppState {
    edgeText: string; // unparsed user-defined edge input
    edges: Edge[]; // parsed edges to draw on the map
}

/**
 * Top-level application that lets the user input edges and display them on
 * a map of the UW Seattle campus
 */
class App extends Component<{}, AppState> { // <- {} means no props.
  constructor(props: any) {
    super(props);
    this.state = {
        edgeText: "",
        edges: []
    };
  }

  /**
   * Updates text display with edge input changes by the user
   */
  onHandleInputChange = (value: string) => {
      this.setState({edgeText: value});
  }

  /**
   * Parses the edge input by the user to display on the map when prompted to draw
   */
  onHandleDrawPressed = () => {
      const edges: Edge[] = [];

      const lines: string[] = this.state.edgeText.split("\n");

      for (let line of lines) {
          if (line.length == 0) {
              continue; // Skips blank lines
          }

          const properties: string[] = line.split(" ");

          if (properties.length !== NUM_EDGE_PROPERTIES) {
              // Clears map to inform user that invalid input was not drawn
              this.setState({edges: []});
              alert("Please enter each edge in the form: x1 y1 x2 y2 color");
              return;
          }

          const x1: number = parseFloat(properties[0]);
          const y1: number = parseFloat(properties[1]);
          const x2: number = parseFloat(properties[2]);
          const y2: number = parseFloat(properties[3]);
          const color: string = properties[4];

          if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2) || !this.isInBounds(x1) ||
              !this.isInBounds(y1) || !this.isInBounds(x2) || !this.isInBounds(y2)) {
              // Clears map to inform user that invalid input was not drawn
              this.setState({edges: []});
              alert(`Please ensure x and y coordinates are numbers between ${UW_MIN_COORD} and ${UW_MAX_COORD}`);
              return;
          }

          edges.push({x1: x1, y1: y1, x2: x2, y2: y2, color: color});
      }

      this.setState({edges});
  }

  /**
   * Checks whether a coordinate is within the bounds of the UW Seattle campus
   */
  isInBounds = (val: number) => {
      return val >= UW_MIN_COORD && val <= UW_MAX_COORD;
  }

  /**
   * Clears the map of edges when prompted to clear
   */
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
          edgeText={this.state.edgeText}
        />
      </div>
    );
  }
}

export default App;
