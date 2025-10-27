# Personal Restaurant Guide

COMP3074 Mobile Application Development - Group-100 Assignment

## Project Description

Android application for managing personal restaurant information. Users can add, edit, and delete restaurant entries with details including name, address, phone number, description, tags, and ratings. The application provides search functionality, location mapping, and sharing capabilities.

## Features Implemented

All 12 requirements from project specification completed:

1. Restaurant storage with add, edit, and delete operations
2. Star rating system (0-5 stars)
3. Google Maps integration for restaurant locations
4. Share restaurant information via email
5. Search by restaurant name or tags
6. Restaurant list view with real-time filtering
7. Detailed restaurant information screen
8. Full-screen embedded map view
9. Navigation directions to restaurant
10. Share functionality from details screen
11. About screen with team information
12. Splash screen with application branding

## Dependencies

- Room: 2.6.1
- Google Maps: 18.2.0
- Maps Compose: 4.3.0
- Play Services Location: 21.1.0
- Navigation Compose: 2.7.7
- Lifecycle ViewModel Compose: 2.7.0
- Material Icons Extended: 1.6.3

## Database Schema

Restaurant table fields:
- id (Primary Key, Auto-increment)
- name (String)
- address (String)
- phone (String)
- description (String)
- tags (String, comma-separated)
- rating (Float, 0-5)
