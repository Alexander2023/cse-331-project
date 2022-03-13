import React, {Component} from "react";
import Dropdown from "./Dropdown";
import {BuildingMap} from "./types";

interface ControlsProps {
    pathStart: string; // building to begin a path
    pathEnd: string; // building to end a path
    onStartDropdownChange(option: string): void; // called when a new option is selected for start dropdown
    onEndDropdownChange(option: string): void; // called when a new option is selected for end dropdown
    onClearPressed(): void; // called when clear button is pressed
}

interface ControlsState {
    buildings: BuildingMap; // buildings to choose from
}

/**
 * A control panel that allows the user to select a start and end building
 * to display a path on the map
 */
class Controls extends Component<ControlsProps, ControlsState> {
    constructor(props: ControlsProps) {
        super(props);

        this.state = {
            buildings: {}
        }
    }

    componentDidMount() {
        this.requestBuildings();
    }

    /**
     * Performs a fetch request for the buildings of the UW campus
     */
    requestBuildings = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildings");

            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }

            let json = await response.json();

            this.setState({
                buildings: json
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    render() {
        return (
            <div id="controls">
                <Dropdown
                    buildings={this.state.buildings}
                    label={"Start:"}
                    selectedOption={this.props.pathStart}
                    onDropdownChange={this.props.onStartDropdownChange}
                />
                <Dropdown
                    buildings={this.state.buildings}
                    label={"End:"}
                    selectedOption={this.props.pathEnd}
                    onDropdownChange={this.props.onEndDropdownChange}
                />
                <button onClick={() => {this.props.onClearPressed()}}>Clear</button>
            </div>
        );
    }
}

export default Controls;
