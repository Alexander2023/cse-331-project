import {Component} from "react";
import {BuildingMap} from "./types";

interface DropdownProps {
    buildings: BuildingMap; // buildings to choose from
    label: string; // label of the dropdown
    selectedOption: string; // dropdown option to display
    onDropdownChange(option: string): void; // called when a new option is selected for dropdown
}

/**
 * A dropdown that allows the user to choose a building
 */
class Dropdown extends Component<DropdownProps, {}> {
    render() {
        return (
            <div>
                <label>
                    {this.props.label}
                    <select
                        value={this.props.selectedOption}
                        onChange={(e) => this.props.onDropdownChange(e.target.value)}>
                        {Object.entries(this.props.buildings).map((names: [string, string], index: number) => {
                            return <option key={index} value={names[0]}>{names[1]}</option>
                        })}
                    </select>
                </label>
            </div>
        );
    }
}

export default Dropdown;