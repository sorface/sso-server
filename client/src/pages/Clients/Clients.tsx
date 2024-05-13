import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { Icon } from '../../components/Icon/Icon';
import { IconNames, pathnames } from '../../constants';

import './Clients.css';

interface App {
  id: string;
  name: string;
}

const apps: App[] = Array.from(
  { length: 5 },
  (_, index) => ({ id: ~~(Math.random() * 1e9) + '', name: `App ${index}` })
);

export const Clients: FunctionComponent = () => {
  const createAppItem = (app: App) => (
    <tr key={app.id}>
      <td>{app.id}</td>
      <td>{app.name}</td>
      <td>
        <Link to={pathnames.clientsEdit.replace(':id', app.id)}>
          <button><Icon name={IconNames.Create} /></button>
        </Link>
      </td>
      <td>
        <button className='remove'><Icon name={IconNames.Remove} /></button>
      </td>
    </tr>
  );

  return (
    <div className='clients'>
      <h3>Clients</h3>
      <table>
        <thead>
          <tr>
            <th>Id</th>
            <th>App Name</th>
            <th className='tight'></th>
            <th className='tight'>
              <Link to={pathnames.clientsAdd}>
                <button className='add'><Icon name={IconNames.Add} /></button>
              </Link>
            </th>
          </tr>
        </thead>
        <tbody>
          {apps.map(createAppItem)}
        </tbody>
      </table>
    </div>
  );
};
