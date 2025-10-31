# Android E-Commerce App

---

## Project Idea  
An Android e-Commerce application built using Jetpack Compose that enables users to register, log in, browse products, search, add to cart, manage wishlist, and checkout. The backend uses Firebase for auth and SQLite for offline storage; product data is fetched via the DummyJSON API.  

---

## Team Members  
- Hossam Ahmed Ali — Login Screen, Sign Up Screen  
- Yousseif Mohamed Ali — Home Screen, Search Screen  
- Yousseif Alaa — Product Screen, Cart Page  
- Essam Mohamed — Settings Screen, Wishlist Screen  
- Kirolos Emad — Landing Page, Forget Password Screen, Profile Screen  

---

## Work Plan  
- **Phase 1: Planning & Discovery (Week 1)**
*   **Objective:** Establish a solid foundation and clear direction for the project.
*   **Key Activities:**
    1.  **Requirement Analysis:** Finalize the feature set for the MVP (Minimum Viable Product) based on the initial idea.
    2.  **Technology Stack Finalization:** Confirm versions for Android SDK, Jetpack Compose, Firebase, Retrofit, SQLite, etc.
    3.  **System Architecture Design:** Sketch a high-level architecture diagram showing how the app components (UI, Data, Domain) interact with Firebase and the DummyJSON API.
    4.  **Audience Personas:** Develop 2-3 detailed user personas (e.g., "Tech-Savvy Sarah," "Budget-Conscious Bob") to guide design and development decisions.
    5.  **Define Project Milestones & Timeline:** Create a Gantt chart or a simple timeline in a shared tool (like Trello, Asana, or GitHub Projects).
*   **Deliverables:** Requirement Specification Document, Technology Stack List, System Architecture Diagram, User Personas, Project Timeline.

- **Phase 2: Design & Prototyping (Week 2)**
*   **Objective:** Create a visual identity and interactive prototype for the application.
*   **Key Activities:**
    1.  **Visual Identity:**
        *   **Logo Design:** Create and finalize the app logo in various resolutions.
        *   **Color Palette & Typography:** Define a consistent color scheme and font families.
    2.  **UI/UX Design:**
        *   Create wireframes for all assigned screens (Landing, Login, Sign Up, Home, etc.).
        *   Develop high-fidelity mockups in a tool like Figma or Adobe XD, incorporating the visual identity.
    3.  **Prototype:** Build an interactive prototype in the design tool to simulate user flows (e.g., login -> home -> product -> add to cart).
    4.  **Main Poster Design:** Create the promotional poster for the project presentation.
*   **Deliverables:** Style Guide, High-Fidelity Figma Mockups, Interactive Prototype, Finalized App Logo, Project Poster.

- **Phase 3: Core Development - Sprints (Weeks 3-7)**
*   **Objective:** Build the core functionality of the application in iterative sprints.
*   **Key Activities (divided by modules):**
    *   **Sprint 1: Foundation & Authentication**
        *   Set up the project structure (Compose, Navigation).
        *   Implement the **Landing, Login, Sign Up, and Forgot Password** screens (Hossam, Krolos).
        *   Integrate **Firebase Authentication**.
    *   **Sprint 2: Product Discovery & Data Layer**
        *   Set up **Retrofit** for networking with the DummyJSON API (Yousseff Mohamed).
        *   Implement the **Home Screen** with product listing (Yousseff Mohamed).
        *   Implement the **Search Screen** with filtering (Yousseff Mohamed).
    *   **Sprint 3: Product Details & User Engagement**
        *   Implement the **Product Detail Screen** (Yousseff Alaa).
        *   Develop the core logic for **Wishlist** (local storage/SQLite) (Essam, Yousseff Alaa).
        *   Develop the core logic for **Shopping Cart** (Yousseff Alaa).
    *   **Sprint 4: User Profile & Settings**
        *   Implement the **Profile Screen** with editing capabilities (Krolos).
        *   Implement the **Settings Screen** (Essam).
        *   Integrate the Wishlist screen UI with its logic (Essam).
    *   **Sprint 5: Checkout & Integration**
        *   Build the **Checkout Flow** (may involve multiple screens).
        *   Integrate the Cart screen with the checkout process (Yousseff Alaa).
        *   Perform initial module integration and bug fixing.
*   **Deliverables:** A functional, feature-complete application after each sprint, with code merged into the main branch on GitHub.

- **Phase 4: Testing, Debugging & Polish (Week 8)**
*   **Objective:** Ensure the application is stable, performant, and user-friendly.
*   **Key Activities:**
    1.  **Unit & Integration Testing:** Write tests for critical business logic (e.g., cart calculations, data parsing).
    2.  **User Acceptance Testing (UAT):** Onboard at least 100 test users (as per KPI) and collect feedback on usability and bugs.
    3.  **Performance Optimization:** Profile the app to ensure it meets the **<2s response time** KPI.
    4.  **Bug Bash:** The entire team tests the app intensively to find and fix crashes and UI glitches.
    5.  **Final Review:** Conduct a final review of all code and user flows.
*   **Deliverables:** Test Report, Bug Fix Log, Performance Profile Report, Polished APK.

- **Phase 5: Launch & Presentation (Week 9)**
*   **Objective:** Showcase the completed project.
*   **Key Activities:**
    1.  **Prepare Final Presentation:** Create slides demonstrating the app's features, architecture, and how it meets the KPIs.
    2.  **Compile Complementary Products:** Gather all final assets (poster, demo video, GitHub repository link).
    3.  **Final Presentation:** Present the project to the instructor and peers.
*   **Deliverables:** Final Presentation Slides, Demo Video, Live Demo, Completed GitHub Repository.  

---

## Roles & Responsibilities  
• Frontend UI with Jetpack Compose (all screens as assigned)  
• Authentication using Firebase (Hossam)  
• Network calls via Retrofit to DummyJSON API (Yousseif Mohamed)  
• Product detail, cart & wishlist modules (Yousseif Alaa)  
• Settings, profile, and wishlist management (Essam)  
• Initial landing, password recovery, and profile editing (Kirolos)  

### KPIs (Key Performance Indicators) – Metrics for project success  
**A. Product & User Engagement KPIs**

1.  **User Adoption & Activation Rate:**
    *   **Definition:** The number of users who successfully register and log into the app.
    *   **Measurement:** Tracked via Firebase Analytics and Authentication logs.
    *   **Target:** At least **100 unique test users** onboarded during the testing phase (UAT).

2.  **User Conversion Rate:**
    *   **Definition:** The percentage of users who start and successfully complete the checkout process.
    *   **Measurement:** Analyze the user flow from adding an item to the cart to completing an order. Can be tracked with custom events in Firebase.
    *   **Target:** **>15%** of users who add an item to their cart complete the checkout flow.

3.  **Feature Engagement:**
    *   **Definition:** Measures how actively users interact with key features like the Wishlist and Search.
    *   **Measurement:** Average number of products added to wishlist per user; number of searches performed per session (Firebase Analytics).
    *   **Target:** **>50%** of active users use the Wishlist feature; **>70%** use the Search function.

**B. Technical Performance KPIs**

1.  **App Responsiveness (Screen Transition Time):**
    *   **Definition:** The average time taken for the app to load and display a new screen after user interaction.
    *   **Measurement:** Use Android Profiler and custom logging to measure screen render times.
    *   **Target:** Average screen transition time **< 2 seconds** on a mid-range device.

2.  **API Response Time:**
    *   **Definition:** The time taken to receive a response from the DummyJSON API after a network request is made.
    *   **Measurement:** Logged via Retrofit interceptors.
    *   **Target:** Critical API calls (e.g., fetching products, user login) respond in **< 1.5 seconds** on average.

3.  **App Stability (Crash-Free Rate):**
    *   **Definition:** The percentage of crash-free user sessions.
    *   **Measurement:** Monitored via Firebase Crashlytics.
    *   **Target:** **> 99%** crash-free sessions during the testing phase and final demo.

**C. Business & Project Success KPIs**

1.  **Project Completion Rate:**
    *   **Definition:** The percentage of planned features (from Phase 1) successfully implemented and functioning in the final build.
    *   **Measurement:** Checklist against the initial Requirement Specification.
    *   **Target:** **100%** of MVP features completed and functional.

2.  **Code Quality:**
    *   **Definition:** Maintainability and cleanliness of the codebase.
    *   **Measurement:** Peer code reviews, adherence to a shared style guide, and absence of major lint warnings.
    *   **Target:** All code is reviewed and merged without major style or architectural issues.
---

## Instructor  
Ahmed Atef

---

## Project Files  
You can find the full project files here:  
👉 [GitHub Repository](https://github.com/Srmortal/ecommerce-app)  

