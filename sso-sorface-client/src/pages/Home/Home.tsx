import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { Captions, pathnames } from '../../constants';

export const Home: FunctionComponent = () => {
  return (
    <div>
      <h2>{Captions.Home}</h2>
      <p>
        <Link to={pathnames.signIn}>{Captions.SignIn}</Link>
      </p>
      <p>
        <Link to={pathnames.logIn}>{Captions.LogIn}</Link>
      </p>
    </div>
  );
};
