import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { Captions, pathnames } from '../../constants';

export const Home: FunctionComponent = () => {
  return (
    <div>
      <h1>{Captions.AppTitle}</h1>
      <p>
        <Link to={pathnames.signUp}>{Captions.SignUp}</Link>
      </p>
      <p>
        <Link to={pathnames.signIn}>{Captions.SignIn}</Link>
      </p>
    </div>
  );
};
