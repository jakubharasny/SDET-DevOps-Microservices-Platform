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
