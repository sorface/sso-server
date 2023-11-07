import { FunctionComponent } from 'react';
import { Routes, Route } from 'react-router-dom';
import { pathnames } from '../constants';
import { Home } from '../pages/Home/Home';
import { NotFound } from '../pages/NotFound/NotFound';
import { SignUp } from '../pages/SignUp/SignUp';
import { SignIn } from '../pages/SignIn/SignIn';

export const AppRoutes: FunctionComponent = () => {
  return (
    <Routes>
      <Route path={pathnames.home} element={<Home />} />
      <Route path={pathnames.signUp} element={<SignUp />} />
      <Route path={pathnames.signIn} element={<SignIn />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};
