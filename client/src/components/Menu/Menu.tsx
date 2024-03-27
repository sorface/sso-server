import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { IconNames, pathnames } from '../../constants';
import { Icon } from '../Icon/Icon';
import { useLogout } from '../../hooks/useLogout';

import './Menu.css';


export const Menu: FunctionComponent = () => {
  const { logoutFetch } = useLogout();

  // TODO: Check user auth
  const disabled = false;

  if (disabled) {
    return <></>;
  }

  const items = [
    {
      path: pathnames.account,
      icon: IconNames.Person,
    },
    {
      path: pathnames.session,
      icon: IconNames.List,
    },
    {
      path: pathnames.account,
      icon: IconNames.Exit,
      onClick: () => { logoutFetch(undefined); },
    },
  ];

  return (
    <div className="menu">
      {items.map(({ icon, path, onClick }, index) => (
        <Link
          key={index}
          to={path}
          {...(onClick && { onClick })}
        >
          <Icon name={icon} />
        </Link>
      ))}
    </div>
  );
};