# Graphical User Interface Prototype  

Authors: Stefano, Mattia, Nicola, Hossein

Date: 18 April 2021

Version: 1.0

## 0 Welcome and Homepages 

![Welcome](../GUI files/welcome.png)

This page is the welcome page. 
Here a subscriber can log in clicking on "Login". Page at point '12' appears.
If a customer wants to register they can click on "Register". Page at point '8' appears.
##

![Normal Subscriber HomePage](../GUI files/account_page.png)

This page is the homepage of a normal subscriber. 
1. The **system** shows some informations in the center: ID card number, fidelity points amount, coupons, if any, and all the personal informations.  
2. The **Subsciber** can modify its own infotmations by clicking on the edit information button. Page at point '7' appears.  
3. The **Subsciber** can request a new card if they lost it, by clicking on the lost card button. The second page at point '8' appears.  
4. The **Subsciber** can click on the log out button on top-right to log out.


##
![Employee HomePage](../GUI files/Employee_Page.png)

This page is the homepage of an employee. It is similar to the Subscriber Homepage, but the Employee has different rights compared to the normal Subscriber.
In addition to the subscribers' rights, they can manage Sales and Inventory, by clicking on the proper buttons on the left. 


##
![Owner HomePage](../GUI files/Owner_Page.png)

This page is the homepage of the Owner. In addition to the subscribers' rights, they can magage Sales, Inventory, Accounting and Rights of all the subscribers, by clicking on the proper buttons on the left.

## 1 Manage sale transaction

![Selling](../GUI files/Selling.png)

This page is described selling steps that Owner or Employee can use this form, products can insert through the barcode scanner.
##
![Reprint](../GUI files/Reprint.png)

This page is used to Reprint the receipt.

## 2 Waring on low stock, Buy product

![Warning on low stock buy](../GUI files/Low_stock_products_owner.png)
First version.

The first version of the view is used to show the products that are low in stock to the owner. Then the **owner** can decide wether buy or not these products.
1. The **owner** can search the products either by barcode or by name, with the searchbox at the bottom.
2. The **system** shows in the table the products that matches with the search criteria, if any.
3. The **owner**, by checking the checkbox relative to the product that he wants to buy, can select the products to which is interested to.
4. The **owner** can choose the quantity for each product by clicking on the realtive number stepper.
5. The **owner** can buy the products by clicking to the order button.

The **owner** can go back to her/his *home* by clicking on the home button.  
The **owner** can log out by clicking on the LogOut button.  
The **owner** can go to the *check inventory* view by clicking on the relative button.  
The **owner** can go to the *add new products* view by clicking on the relative button.  
The **owner** can go to the *edit product information* view by clicking on the relative button.  
The **owner** can go to the *mark product as not sold anymore* view by clicking on the relative button.  

![Warning on low stock](../GUI files/Low_stock_products_employee.png)
second version.
## 7 Edit informations account

![Edit informations account](../GUI files/edit_info.png)

This is the page in which the subscriber can modify its own informations, and then committ by clicking on the "Save button".
Name, Surname, SSN and date of birth cannot be modified.
On the top left there is the button to go back to the Homepage, on the top right there is the button to log out.


## 8 Registration, Request fidelity card

![Create account](../GUI files/create_account.png)

This page is used to create a new account. 
The customer can insert all the informations and click on Save. All the fields with * are required. 
##
![Request new card](../GUI files/request_new_card.png)

From the Homepage, after clicking on "Lost Card", this page appears. Here the subsciber can request a new fidelity card in case they lost it. The old card is automatically marked as lost and all the points and coupons are transferred to the new card.

##
![Card Created](../GUI files/new_card_created.png)

This is the page that confirms with a pop up the creation of the new card.

## 9 Create Coupon

![Create coupon](../GUI files/create_coupon_page.png)

This page is used to create a coupon that appears after clicking on "Create Coupon" in the Homepage. 
Here the subscriber can choose the amount of the coupon and click on "create coupon".

##
![Coupon Created](../GUI files/coupon_created.png)

This page shows the pop up that appears after the creation of the coupon.

## 10 Check Points

![Check Points](../GUI files/account_page.png)

This is the homepage where the subsciber can check its own points in the top rectangle.


## 12 Log in 

![Login](../GUI files/login.png)

This is the login page. The subscriber can insert email and password and then click on "Login".
##
![Login error](../GUI files/login__error.png)

This is the error shown when the credentials are wrong.


## 13 Manage Rights

![Manage Rights](../GUI files/Manage_Rights.png)

In this page the owner can change the subscribers' rights by clicking in the left box near the name: 'O' for the Owner, 'E' for the Employee, 'S' for the normal subsciber. The "Save" button on the bottom-right saves all the changes.

## 14 Accounting and sale transactions
![Accounting home page](../GUI files/Accounting_home_page.png)

This is the main accounting page from which the owner can access to all the accounting features.
##
![Sale transactions](../GUI files/List_of_sale_transactions.png)

This page contains the list of sale transactions. The report button will lead to the report page.

## 15 Expenses
![Expenses](../GUI files/List_of_expenses.png)

This page contains the list of expenses

## 16 Salaries
![Salaries](../GUI files/Pay_Salaries.png)

This page contais the list of employees and respective salaries. The pay buttow will open the bank web site and allow the owner to easily pay the salaries.

## 17 Forgot password

![Forgot passowrd](../GUI files/forgot_password.png)

This page is used to reset the password in the case the subscriber forgot it.
##
![Forgot password error](../GUI files/forgot_password_error.png)

This is the error shown when the subscriber isn't in the system.

\<Report here the GUI that you propose. You are free to organize it as you prefer. A suggested presentation matches the Use cases and scenarios defined in the Requirement document. The GUI can be shown as a sequence of graphical files (jpg, png)  >

