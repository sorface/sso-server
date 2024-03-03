import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';

import './App.css';
import { REACT_APP_VERSION } from './config';
import { REACT_APP_BACKEND_URL } from './config';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <div className="App-content">
          <AppRoutes />
        </div>
        <footer>
          <div>Version: {REACT_APP_VERSION}/ Secure url: {REACT_APP_BACKEND_URL}</div>
        </footer>
      </div>
    </BrowserRouter>
  );
}

export default App;
