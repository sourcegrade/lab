import Image from 'next/image'
import Button from '@mui/material/Button';

// Assuming you have an Order type defined somewhere
type Order = {
  creator: {
    username: string;
    profilePicture: string;
  };
  titleImage: string;
  sumOfPurchasedItems: number;
};

export default function Home() {
  // This would be fetched from an API in a real application
  const orders: Order[] = [
    {
      creator: {
        username: 'John Doe',
        profilePicture: 'url-to-john-doe-profile-picture',
      },
      titleImage: 'url-to-title-image',
      sumOfPurchasedItems: 5,
    },
    // More orders here...
  ];

  return (
    <div>
      <h1>Recent Orders</h1>
      {orders.map((order, index) => (
        <div key={index}>
          <Image src={order.creator.profilePicture} alt={order.creator.username} />
          <p>{order.creator.username}</p>
          <Image src={order.titleImage} alt="Title image" />
          <p>{order.sumOfPurchasedItems}</p>
        </div>
      ))}
    </div>
  );
}