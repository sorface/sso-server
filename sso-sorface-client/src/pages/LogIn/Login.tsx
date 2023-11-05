import { FormEvent, FunctionComponent, Fragment, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useApiMethod } from '../../hooks/useApiMethod';
import { loginApiDeclaration } from '../../apiDeclarations';
import { Captions, pathnames } from '../../constants';

const fields = [
  {
    name: 'email',
    placeholder: Captions.Email,
    autoComplete: 'username',
    required: true,
  },
  {
    name: 'password',
    placeholder: Captions.Password,
    type: 'password',
    autoComplete: 'current-password',
    required: true,
  },
];

export const LogIn: FunctionComponent = () => {
  const { apiMethodState, fetchData } = useApiMethod<unknown, FormData>(loginApiDeclaration.login);
  const { process: { loading, error }, data } = apiMethodState;

  useEffect(() => {
    if (!data) {
      return;
    }
    alert(`Response data: ${JSON.stringify(data)}`)
  }, [data]);

  const handleSignIn = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const data = new FormData(form);
    fetchData(data);
  };

  return (
    <div>
      <h2><Link to={pathnames.home}>{Captions.BackSymbol}</Link> {Captions.LogIn}</h2>
      <form onSubmit={handleSignIn}>
        {fields.map(field => (
          <Fragment key={field.name}>
            <label htmlFor={field.name}><p>{field.placeholder}:</p></label>
            <input id={field.name} {...field} />
          </Fragment>
        ))}
        <p>
          <input type="submit" value={Captions.LogIn} />
        </p>
        <p>
          {loading && <span>{Captions.Loading}...</span>}
          {error && <span>{Captions.Error}: {error}</span>}
        </p>
      </form>
    </div>
  );
};
