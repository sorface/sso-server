import {FunctionComponent} from 'react';
import {Captions, pathnames} from '../../constants';
import {Link} from 'react-router-dom';

export const NotFound: FunctionComponent = () => {
    return (
        <div>
            <p>{Captions.NotFound}</p>
            <p><Link to={pathnames.home}>{Captions.Home}</Link></p>
        </div>
    );
};
