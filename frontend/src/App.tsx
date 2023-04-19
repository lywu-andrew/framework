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
  newApp = async () => {
    const response = await fetch('/newapp');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param i
   * @returns 
   */
  play(i: number, type: string): React.MouseEventHandler {
    if (type === 'data') {
      return async (e) => {
        // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
        e.preventDefault();
        const response = await fetch(`/selectData?i=${i}`)
        const json = await response.json();
        this.setState(json);
      }
    } else {
      return async (e) => {
        // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
        e.preventDefault();
        const response = await fetch(`/selectVis?i=${i}`)
        const json = await response.json();
        this.setState(json);
      }
    }
  }

  createCell(cell: Cell, index: number, type: string): React.ReactNode {
    if (cell.selected)
      return (
        <div key={index}>
          <a href='/' onClick={this.play(cell.i, type)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    else
      return (
        <div key={index}><BoardCell cell={cell}></BoardCell></div>
      )
  }

  getImage() : React.ReactNode {
      if (this.state.imgPath == null) return;
      else return (
        <img className="visualization" src={require("../../backend" + this.state.imgPath)} alt={"visualization"}></img>
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
      this.newApp();
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
        <div id="left-bar">
          {this.state.dataCells.map((cell, i) => this.createCell(cell, i, 'data'))}
        </div>
        <div id="right-bar">
          {this.state.visCells.map((cell, i) => this.createCell(cell, i, 'vis'))}
        </div>
        <div id="img">
          {this.getImage()}
        </div> 
        <div id="bottombar">
          <button onClick={/* get the function, not call the function */this.newApp}>New App</button>
        </div>
      </div>
    );
  }
}

export default App;
