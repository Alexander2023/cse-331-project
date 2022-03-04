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

interface EdgeListProps {
    edgeText: string; // edges in the text field
    onChange(edgeText: string): void;  // called when a new edge list is ready
    onDrawPressed(): void; // called when draw button is pressed
    onClearPressed(): void; // called when clear button is pressed
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps> {
    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={(e) => {this.props.onChange(e.target.value)}}
                    value={this.props.edgeText}
                /> <br/>
                <button onClick={() => {this.props.onDrawPressed()}}>Draw</button>
                <button onClick={() => {this.props.onClearPressed()}}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
