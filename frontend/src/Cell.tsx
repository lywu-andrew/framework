import React from 'react';
import { Cell } from './state';

interface Props {
  cell: Cell
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const selected = this.props.cell.selected ? 'selected' : '';
    return (
      <div className={`cell ${selected}`}>{this.props.cell.name}</div>
    )
  }
}

export default BoardCell;