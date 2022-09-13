package nz.ac.uclive.shopport

// TODO: Remove this file and use Room
data class WishlistItemClass(val id: Int, val title: String, val description: String, val price: Int, val location: String, val imageId: String)
val wishlistItems = listOf(
    // Items for the wishlist
    WishlistItemClass(1, "Item 1", "Description 1", 10, "Location 1", "https://picsum.photos/200/300"),
    WishlistItemClass(2, "Item 2", "Description 2", 20, "Location 2", "https://picsum.photos/200/300"),
    WishlistItemClass(3, "Item 3", "Description 3", 30, "Location 3", "https://picsum.photos/200/300"),
    WishlistItemClass(4, "Item 4", "Description 4", 40, "Location 4", "https://picsum.photos/200/300"),
    WishlistItemClass(5, "Item 5", "Description 5", 50, "Location 5", "https://picsum.photos/200/300"),
)