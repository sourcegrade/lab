import Image from "next/image"
import Button from "@mui/material/Button";

// Assuming you have an Order type defined somewhere
interface Order {
  creator: {
    username: string;
    profilePicture: string;
  };
  titleImage: string;
  sumOfPurchasedItems: number;
}

export default function Home() {
  // This would be fetched from an API in a real application
  const orders: Order[] = [
    {
      creator: {
        username: "John Doe",
        profilePicture: "url-to-john-doe-profile-picture",
      },
      titleImage: "url-to-title-image",
      sumOfPurchasedItems: 5,
    },
    // More orders here...
  ];

  return (
    <div>
      <h1>Recent Orders</h1>
      {orders.map((order, index) => (
        <div key={index}>
          <Image alt={order.creator.username} src={order.creator.profilePicture} />
          <p>{order.creator.username}</p>
          <Image alt="Title image" src={order.titleImage} />
          <p>{order.sumOfPurchasedItems}</p>
        </div>
      ))}
    </div>
  );
}