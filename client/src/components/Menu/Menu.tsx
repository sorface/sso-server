import { FunctionComponent, useContext } from 'react';
import { Link } from 'react-router-dom';
import { IconNames, pathnames } from '../../constants';
import { Icon } from '../Icon/Icon';
import { useLogout } from '../../hooks/useLogout';
import { AuthContext } from '../../context/AuthContext';

import './Menu.css';


export const Menu: FunctionComponent = () => {
  const account = useContext(AuthContext);
  const { logoutFetch } = useLogout();

  const disabled = !account;

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
      path: pathnames.clients,
      icon: IconNames.Apps,
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
