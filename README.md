# 🛒 Android E-Commerce App

This is a simple Android-based **E-Commerce application** built by our team.  
The app allows users to **register, log in, browse products, search, add to cart, manage wishlist, and checkout**.  

We use:  
- **Firebase** → for user authentication (Login, Register, Forget Password).  
- **DummyJSON API** → for product data ([Documentation](https://dummyjson.com/docs)).  
- **Jetpack Compose** → for modern Android UI.  
- **Retrofit** → for network requests to the DummyJSON API.  
- **SQLite** → for local storage (cart, wishlist, caching).  

---

## 👥 Team Members & Responsibilities

### 1. **Hossam Ahmed Ali**
- **Login Screen**  
  Users can log in using their email and password.  
- **Sign Up Screen**  
  New users can create an account by entering their name, email, password, location, and confirming the password.  

👉 Handles Firebase Authentication (Login/Register), form validation, and navigation flow after authentication.

---

### 2. **Yousseif Mohamed Ali**
- **Home Screen**  
  Displays main products, categories, offers, and includes a search bar for quick access.  
- **Search Screen**  
  Allows users to search for products by name or category, view instant results, and apply filters to refine their search.  

👉 Displays featured products, categories, and integrates search functionality using Retrofit and DummyJSON API.

---

### 3. **Yousseif Alaa**
- **Product Screen**  
  Displays detailed information about a selected product, including images, price, description, and “Add to Cart” button.  
- **Cart Page**  
  Shows the list of selected products, their quantities, total price, and checkout button.  

👉 Manages product details, add-to-cart logic, and shopping cart functionality using Retrofit + SQLite.

---

### 4. **Essam Mohamed**
- **Settings Screen**  
  Allows users to edit profile details, change language, or log out.  
- **Wishlist Screen**  
  Shows all the products that users have added to their favorites for easy access later.  

👉 Implements profile preferences, app settings (like language, theme, logout), and manages favorite products using SQLite.

---

### 5. **Kirolos Emad**
- **Landing Page**  
  The first page that introduces the app or store, highlights key features, and directs users to sign up or browse.  
- **Forget Password Screen**  
  Users can reset their password by entering their email or phone number to receive a verification code or reset link.  

👉 Designs the app’s introductory screen and handles password reset using Firebase’s password recovery flow.

---

## ⚙️ Tech Stack

- **Language**: Kotlin  
- **UI Framework**: Jetpack Compose  
- **Networking**: Retrofit  
- **Backend API**: [DummyJSON](https://dummyjson.com/docs)  
- **Authentication**: Firebase Auth  
- **Database/Storage**:  
  - **Firebase Firestore** → optional for user orders and cloud sync  
  - **SQLite** → for offline/local storage (cart, wishlist, cached products)  

---

## 📌 Features

- **User Authentication**: Login, Register, Forgot Password using Firebase.  
- **Product Browsing**: Display categories and products using Retrofit with DummyJSON API.  
- **Product Details**: View product info, images, and price.  
- **Cart & Wishlist**: Add or remove products using SQLite for offline storage.  
- **Search**: Find products by name or category.  
- **Checkout**: Complete orders and store them in Firebase Firestore.  
- **Orders**: View past orders and details.  
- **Profile & Settings**: Manage account and app preferences.  
- **Help/Contact**: Reach support or view help information.  

---

## ✅ Task Distribution Table

| Member               | Screens / Modules Assigned                                       |
|----------------------|------------------------------------------------------------------|
| **Hossam Ahmed Ali** | Splash, Login, Register, Forgot Password                         |
| **Yousseif Mohamed Ali** | Home, Category, Search                                       |
| **Yousseif Alaa**    | Product Details, Cart, Wishlist                                  |
| **Essam Mohamed**    | Profile, Settings, Checkout                                     |
| **Kirolos Emad**     | Orders, Order Details, Help / Contact Us                         |

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Srmortal/ecommerce-app
