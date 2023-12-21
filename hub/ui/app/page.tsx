import Image from 'next/image'
import Button from '@mui/material/Button';
import { Icon } from '@mui/material';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import FormatListBulletedIcon from '@mui/icons-material/FormatListBulleted';

export default function Page() {
  return (
      <div>
        <div className='flex justify-center space-x-6 p-3 rounded-md bg-slate-700'>
          <Button variant="contained" className="flex-col flex-grow">
            <AddCircleIcon className='!text-5xl'/>
            Create new Course
            </Button>
          <Button variant="contained" className="flex-col flex-grow">
            <FormatListBulletedIcon className='!text-5xl'/>
            Show my Courses
            </Button>
        </div>
        <h1>Recent Courses</h1>

        <h1>Recent Assignments</h1>
      </div>
  )
}
