interface State {
  dataCells: Cell[];
  visCells: Cell[];
  imgPath: string | null; // imgPath maybe null
}

interface Cell {
  i: number;
  name: string;
  selected: boolean;
}

export type { State, Cell }