import { FunctionComponent } from 'react';
import { useParams } from 'react-router-dom';

export const ClientsEdit: FunctionComponent = () => {
  const { id } = useParams();

  return (
    <div>
      <h3>Client Edit</h3>
      <div>Client id: {id}</div>
    </div>
  )
};
