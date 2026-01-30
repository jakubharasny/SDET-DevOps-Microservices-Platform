CREATE TABLE UserRole (
    Id bigint NOT NULL AUTO_INCREMENT,
    Name varchar(100) NOT NULL,
    Description varchar(200) NULL,
    IsEnabled bit NOT NULL,
    Created date NOT NULL,
    CreatedBy varchar(200) NOT NULL,
    Updated date NULL,
    UpdatedBy varchar(200) NULL,
    CONSTRAINT PK_UserRole PRIMARY KEY (Id ASC)
);

CREATE TABLE UserCredit (
    Id bigint NOT NULL AUTO_INCREMENT,
    UserRoleId bigint NOT NULL,
    Name varchar(100) NOT NULL,
    CreditLimit decimal(12, 2) NOT NULL,
    CurrencyCode char(3) NOT NULL,
    CardProvider varchar(50) NOT NULL,
    Created date NOT NULL,
    CONSTRAINT PK_UserCredit PRIMARY KEY (Id ASC),
    CONSTRAINT FK_UserCredit_UserRole FOREIGN KEY (UserRoleId) REFERENCES UserRole (Id)
);
