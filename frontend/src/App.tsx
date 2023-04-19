import React from 'react';
import './App.css'; // import the css file to enable your styles.
import { State, Cell } from './state';
import BoardCell from './Cell';

/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 * 
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 * 
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */
class App extends React.Component<Props, State> {
  private initialized: boolean = false;

  /**
   * @param props has type Props
   */
  constructor(props: Props) {
    super(props)
    /**
     * state has type State as specified in the class inheritance.
     */
    this.state = { dataCells: [], visCells: [], imgPath: null}
  }

  /**
   * Use arrow function, i.e., () => {} to create an async function,
   * otherwise, 'this' would become undefined in runtime. This is
   * just an issue of Javascript.
   */
  newGame = async () => {
    const response = await fetch('/newgame');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * pickCard will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
  pickCard(i: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/pickcard?i=${i}`)
      const json = await response.json();
      this.setState(json);
    }
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
  play(i: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/play?x=${x}&y=${y}`)
      const json = await response.json();
      this.setState(json);
    }
  }

  createCell(cell: Cell, index: number): React.ReactNode {
    if (cell.selected)
      return (
        <div key={index}>
          <a href='/' onClick={this.play(cell.i)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    else
      return (
        <div key={index}><BoardCell cell={cell}></BoardCell></div>
      )
  }

  /**
   * This function will call after the HTML is rendered.
   * We update the initial state by creating a new app.
   * @see https://reactjs.org/docs/react-component.html#componentdidmount
   */
  componentDidMount(): void {
    /**
     * setState in DidMount() will cause it to render twice which may cause
     * this function to be invoked twice. Use initialized to avoid that.
     */
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  /**
   * The only method you must define in a React.Component subclass.
   * @returns the React element via JSX.
   * @see https://reactjs.org/docs/react-component.html
   */
  render(): React.ReactNode {
    /**
     * We use JSX to define the template. An advantage of JSX is that you
     * can treat HTML elements as code.
     * @see https://reactjs.org/docs/introducing-jsx.html
     */
    return (
      <div>
        <div id="instructions">{this.state.instruction}</div>
        <div id="board">
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="cardbar">
          <button onClick={this.pickCard(0)}>Demeter</button>
          <button onClick={this.pickCard(1)}>Minotaur</button>
          <button onClick={this.pickCard(2)}>Pan</button>
          <button onClick={this.pickCard(3)}>Player</button>
        </div>
        <div id="bottombar">
          <button onClick={/* get the function, not call the function */this.newGame}>New Game</button>
        </div>
      </div>
    );
  }
}

export default App;
