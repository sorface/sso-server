import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { IconNames, pathnames } from '../../constants';
import { Icon } from '../Icon/Icon';

import './Menu.css';

const items = [
  {
    path: pathnames.account,
    icon: IconNames.List,
  },
  {
    path: pathnames.account,
    icon: IconNames.Person,
  },
  {
    path: pathnames.account,
    icon: IconNames.Exit,
  },
];

export const Menu: FunctionComponent = () => {
  // TODO: Check user auth
  const disabled = false;

  if (disabled) {
    return <></>;
  }

  return (
    <div className="menu">
      {items.map(({ icon, path }, index) => (
        <Link key={index} to={path}><Icon name={icon} /></Link>
      ))}
    </div>
  );
};
