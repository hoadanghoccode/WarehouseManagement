-- create database MaterialManagement

-- use MaterialManagement

-- drop database MaterialManagement 

-- Bảng Role
CREATE TABLE Role (
    Role_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50),
    Description VARCHAR(200)
);

-- Bảng Resource
CREATE TABLE Resource (
    Resource_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50),
    Description VARCHAR(250),
    Created_at DATETIME,
    Updated_at DATETIME
);

-- Bảng Resource_role
CREATE TABLE Resource_role (
    Resource_role_id INT AUTO_INCREMENT PRIMARY KEY,
    Resource_id INT,
    Role_id INT,
    Can_add CHAR(1),
    Can_view CHAR(1),
    Can_create CHAR(1),
    Can_delete CHAR(1),
    Created_at DATETIME,
    Updated_at DATETIME,
    FOREIGN KEY (Role_id) REFERENCES Role(Role_id),
    FOREIGN KEY (Resource_id) REFERENCES Resource(Resource_id)
);

-- Bảng Branch
CREATE TABLE Branch (
    Branch_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50),
    Address VARCHAR(200),
    Hotline VARCHAR(15)
);

-- Bảng Warehouse
CREATE TABLE Warehouse (
    Warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    Branch_id INT NOT NULL,
    Name VARCHAR(50),
    Address VARCHAR(200),
    FOREIGN KEY (Branch_id) REFERENCES Branch(Branch_id)
);

-- Bảng Users
CREATE TABLE Users (
    User_id INT AUTO_INCREMENT PRIMARY KEY,
    Role_id INT NOT NULL,
    Branch_id INT NOT NULL,
    Full_name VARCHAR(50),
    Password VARCHAR(255),
    Image VARCHAR(500),
    Gender BOOLEAN,
    Email VARCHAR(50) NOT NULL,
    Phone_number VARCHAR(10),
    Address TEXT,
    Date_of_birth DATE,
    Created_at DATETIME,
    Updated_at DATETIME,
    Status BOOLEAN NOT NULL,
    Reset_Password_Token TEXT,
    Reset_Password_Expiry DATETIME,
    FOREIGN KEY (Role_id) REFERENCES Role(Role_id),
    FOREIGN KEY (Branch_id) REFERENCES Branch(Branch_id)
);

-- Bảng Group
CREATE TABLE `Group` (
    Group_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Role_id INT,
    FOREIGN KEY (Role_id) REFERENCES Role(Role_id)
);

-- Bảng Group_has_User
CREATE TABLE Group_has_User (
    Group_id INT NOT NULL,
    User_id INT NOT NULL,
    PRIMARY KEY (Group_id, User_id),
    FOREIGN KEY (Group_id) REFERENCES `Group`(Group_id),
    FOREIGN KEY (User_id) REFERENCES Users(User_id)
);

-- Bảng Export_note
CREATE TABLE Export_note (
    Export_note_id INT AUTO_INCREMENT PRIMARY KEY,
    User_id INT NOT NULL,
    Warehouse_id INT,
    Created_at DATETIME,
    Customer_name VARCHAR(50),
    FOREIGN KEY (User_id) REFERENCES Users(User_id),
    FOREIGN KEY (Warehouse_id) REFERENCES Warehouse(Warehouse_id)
);

-- Bảng Category (có phân cấp)
CREATE TABLE Category (
    Category_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Parent_id INT DEFAULT NULL,
    FOREIGN KEY (Parent_id) REFERENCES Category(Category_id)
);

-- Bảng Unit
CREATE TABLE Unit (
    Unit_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);

-- Bảng Material
CREATE TABLE Material (
    Material_id INT AUTO_INCREMENT PRIMARY KEY,
    Category_id INT NOT NULL,
    Unit_id INT NOT NULL,
    Name VARCHAR(250),
    Unit_of_calculation VARCHAR(50),
    Inventory_quantity INT,
    FOREIGN KEY (Category_id) REFERENCES Category(Category_id),
    FOREIGN KEY (Unit_id) REFERENCES Unit(Unit_id)
);

-- Bảng Supplier
CREATE TABLE Supplier (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100)
);

-- Bảng SupplierMaterial (N-N)
CREATE TABLE SupplierMaterial (
    SupplierId INT,
    MaterialId INT,
    PRIMARY KEY (SupplierId, MaterialId),
    FOREIGN KEY (SupplierId) REFERENCES Supplier(Id),
    FOREIGN KEY (MaterialId) REFERENCES Material(Material_id)
);

-- Bảng Export_note_detail
CREATE TABLE Export_note_detail (
    Export_note_id INT,
    Material_id INT NOT NULL,
    Quantity INT,
    Unit_price INT,
    PRIMARY KEY (Material_id, Export_note_id),
    FOREIGN KEY (Export_note_id) REFERENCES Export_note(Export_note_id),
    FOREIGN KEY (Material_id) REFERENCES Material(Material_id)
);

-- Bảng Orders
CREATE TABLE Orders (
    Order_id INT AUTO_INCREMENT PRIMARY KEY,
    Warehouse_id INT NOT NULL,
    User_id INT NOT NULL,
    Created_at DATETIME,
    Supplier VARCHAR(200),
    FOREIGN KEY (Warehouse_id) REFERENCES Warehouse(Warehouse_id),
    FOREIGN KEY (User_id) REFERENCES Users(User_id)
);

-- Bảng Order_detail
CREATE TABLE Order_detail (
    Material_id INT NOT NULL,
    Order_id INT NOT NULL,
    Quantity INT,
    Unit_price INT,
    PRIMARY KEY (Material_id, Order_id),
    FOREIGN KEY (Material_id) REFERENCES Material(Material_id),
    FOREIGN KEY (Order_id) REFERENCES Orders(Order_id)
);

-- Bảng Import_note
CREATE TABLE Import_note (
    Import_note_id INT AUTO_INCREMENT PRIMARY KEY,
    Order_id INT NOT NULL,
    User_id INT NOT NULL,
    Warehouse_id INT NOT NULL,
    Created_at DATETIME,
    FOREIGN KEY (Warehouse_id) REFERENCES Warehouse(Warehouse_id),
    FOREIGN KEY (User_id) REFERENCES Users(User_id),
    FOREIGN KEY (Order_id) REFERENCES Orders(Order_id)
);

-- Bảng Import_note_detail
CREATE TABLE Import_note_detail (
    Import_note_id INT,
    Material_id INT,
    Quantity INT,
    Unit_price INT,
    PRIMARY KEY (Import_note_id, Material_id),
    FOREIGN KEY (Import_note_id) REFERENCES Import_note(Import_note_id),
    FOREIGN KEY (Material_id) REFERENCES Material(Material_id)
);





