---
title: Hệ quản trị CSDL
tags: ['SQL']
---

# Xây dựng CSDL

```sql
-- Tạo CSDL
CREATE DATABASE DatabaseName -- tối đa 123 char
[ON [PRIMARY] (file_spec) [,...n] ]
[LOG ON (file_spec) [,...n] ]

Cấu trúc file_spec: 
NAME = logical_file_name,
FILENAME = os_file_name,
SIZE = size, -- (Kích thước ban đầu cho các file dữ liệu. Mặc định 1 MB, tối thiểu 512 KB)
MAXSIZE = { max_size | UNLIMITED }, -- Kích thước tối đa cho các file dữ liệu. Mặc định là vô tận cho đến khi hết đĩa cứng.Tối thiểu 3MB
FILEGROWTH = growth_increment -- Bước tăng kích thước file. Tối thiểu 64 KB, mặc định 10%. Giá trị phần trăm tính trên kích thước hiện hành

-- Sửa tên CSDL
ALTER DATABASE AdventureWorks2012 
Modify Name = Northwind

-- Thêm file
ALTER DATABASE [QLDA] 
ADD FILE (
    NAME = N'QLDA_Data2', 
    FILENAME = N'C:\Program Files\Microsoft SQLServer\MSSQL.1\MSSQL\DATA\QLDA_Data2.ndf', 
    SIZE = 2048KB, 
    FILEGROWTH = 1024KB
) 

-- Sửa file
Alter database QLDA
Modify File (
    Name = QLDA_data, 
    NewName = QLDA1, FileName ='D:\myfile.ndf',
    SIZE = 200MB
) 

-- Xóa file
Alter database QLDA
Remove File QLDA1

-- Schema
-- Bảo mật
-- Gom nhóm các đối tượng
-- Giới hạn quyền thao tác của user
CREATE SCHEMA <schema_name> [AUTHORIZATION <owner_name>] -- tạo schema
DROP SCHEMA <schema_name> -- xóa schema
-- chuyển bảng sang schema khác
ALTER SCHEMA <new_schema>
TRANSFER <old_schema>.<table_name>

-- Thêm thuộc tính
Alter table SinhVien
Add Phai nvarchar(5) not null default N'Nữ'
-- Sửa thuộc tính
Alter table SinhVien
Alter column DiaChi nvarchar(50)

-- Kiểu tự định nghĩa
-- tạo
sp_addtype [@typename =] type, [@phystype =] system_data_type, [[ @nulltype = ] 'null_type'], [[ @owner = ] 'owner_name'] 
EXEC sp_addtype CMND, 'varchar(11)', 'NOT NULL' 
-- xóa
EXEC sp_droptype <type_name> 

-- Thủ tục hệ thống
-- Xem kích thước CSDl
sp_helpdb 'TênCSDL'
-- xem danh sách CSDL
Exec sp_databases
Select * from sys.objects where type = 'U' -- xem User defined database
-- Xem danh sách Table và View
Exec sp_tables ['table_name'], ['owner'], ['database_name'], ["'type'"] -- type: TABLE, SYSTEM TABLE, VIEW
-- Xem danh sách Column
Exec sp_columns table_name, [owner], [database], [column]
-- Xem danh sách ràng buộc
Exec sp_helpconstraint table_name
-- Cho biết các thông tin về đối tượng bất kỳ trong bảng sysobjects hoặc systypes
Exec sp_help [object_name]

-- Cập nhật dữ liệu
Update HangHoa set 
DonGia = DonGia*110/100
output deleted.DonGia, inserted.dongia
where LoaiHang = 'Milk'

Insert Top(@n) into LoaiDiaOc(maLoai, tenLoai)
select maLoai, tenLoai from LoaiDiaOc2
```

# Truy vấn nâng cao

```sql
-- Khai báo biến
Declare @var_name datatype
-- Biến hệ thống có ý nghĩa trên cả hệ thống. Tên của chúng bắt đầu bằng @@. Các biến này là read-only

-- if else
Declare @SiSo int
Select @SiSo = SiSo
From HocPhan Where MaHP= 'HP01'
If @SiSo < 50
Begin
Insert into DANGKY(MaSV, MaHP)
Values('001', 'HP01')
Print N'Đăng ký thành công'
End
Else
Print N'Học phần đã đủ SV'

-- while
Declare @STT int
Set @STT = 1
While exists (select * from SV 
where MaSV = @STT)
set @STT = @STT+1
Insert into SV(MaSV, HoTen)
values(@STT, 'Nguyen Van A')

-- case
Select MaNV, HoTen, 'Loai' = Case 
when CapBac<=3 then 'Binh Thuong'
when CapBac is null then 'Chua xep loai'
else 'Cap Cao' End
From NhanVien

-- Stored Procedure
-- thêm
Create proc usp_ThemDangKy
@MaSV char(5),
@MaHP char(5),
@SiSo int = null output
As
Select @SiSo = SiSo From HocPhan Where MaHP= @MaHP
if @SiSo < 50
Begin
insert into DANG_KY(MaSV, MaHP)
values(@MaSV, @MaHP)
set @SiSo = @SiSo+1
return 1
End
return 0

Exec @Result = usp_ThemDangKy 'AB001', 'MTH01', @SiSo output 

-- sửa: Thay create = alter
-- xóa
Drop proc usp_ThemDangKy

-- Khai báo kiểu dữ liệu mới
CREATE TYPE DSCTDonHang AS TABLE
(
    MaSP int UNIQUE,
    DonGia float,
    SoLuong int
)
declare @temp DSCTDonHang
insert @temp values(1, 2, 4)
-- Sử dụng
create proc usp_thempdh
    @temp as DSCTDonHang readonly,
    ...



-- Cursor

--- Khai báo
-- cú pháp chuẩn
Declare cur_name [Insensitive] [Scroll] Cursor
Set @cur = Cursor
For select_statement
[For {Read only| Update [of column_name [,…n]] }]
-- cú pháp mở rộng
Declare cursor_name Cursor
-- local: chỉ có thể sử dụng trong phạm vi một query batch hoặc một thủ tục/hàm
-- Global:tồn tại trong suốt connection hoặc đến khi bị hủy tường minh. [Mặc định]
[Local | Global ]
-- Forward_only: cursor chỉ có thể duyệt một chiều từ đầu đến cuối. [Mặc định]
-- Scroll: có thể duyệt lên xuống cursor tùy ý (next, prior, first, last)
[Forward_only| Scroll]
-- static: nội dung của cursor không thay đổi trong suốt thời gian tồn tại
-- dynamic: nội dung của cursor có thể thay đổi trong thời gian tồn tại nếu dữ liệu 
-- trong các bảng liên quan có thay đổi. [Mặc định]
[Static| Dynamic]
[Read_only]
For select_statement
Set @cur = Cursor
[For Update [of column_name [,…n]]]

--- Duyệt cursor
Fetch
[
    [Next| Prior| First| Last| Absolute n| Relative n] -- Mặc đinh là Next
    From 
] Tên_cursor
[Into @Tên_biến [,…n] ]
-- @@fetch_status = 0 là fetch thành công

--- Sử dụng
-- Open tên_cursor
-- Duyệt Cursor
-- Lặp lại việc duyệt, sử dụng  @@fetch_status để xem duyệt hết cursor chưa
-- Close Tên_cursor
-- Deallocate Tên_cursor

Declare cur_DSKhoa Cursor
For Select MaKhoa, TenKhoa From Khoa
Open cur_DSKhoa
Declare @MaKhoa int, @TenKhoa varchar(30)
Fetch Next From cur_DSKhoa into @MaKhoa, @TenKhoa
While @@fetch_status = 0
    Begin
    update SinhVien 
    set MaSV = MaKhoa +MaSV
    Where MaKhoa = @MaKhoa
    
    Fetch Next From cur_DSKhoa into @MaKhoa, @TenKhoa
End
Close cur_DSKhoa
Deallocate cur_DSKhoa

-- Function
--- Chấp nhận nhiều kiểu giá trị trả về
--- Không chấp nhận tham số output
--- Gồm 3 loại: 
--- - Giá trị trả về là kiểu dữ liệu cơ sở. Khi dùng phải có tên owner của hàm
Create function func_name( {parameter_name DataType [= default ]} [,…n])
Returns DataType
As
Begin
    ...
    Return {value | variable | expression}
End
--- - Giá trị trả về là Table có được từ một câu truy vấn
Create function func_name( {parameter_name DataType [= default ]} [,…n])
Returns Table
As
Return select_statement
--- - Giá trị trả về là table mà dữ liệu có được nhờ tích lũy dần sau một chuỗi thao tác xử lý
Create function func_name( {parameter_name DataType [= default ]} [,…n])
Returns TempTab_name Table(Table_definition)
As
Begin
    ...
    Return
End

-- Ví dụ
Create function uf_DanhSachLop
Returns @DS 
Table(MaLop varchar(10), SoSV int)
As
Declare cur_L cursor for Select Ma From Lop
Declare @Ma varchar(10)
Open cur_L
Fetch next from cur_L into @Ma
While @@fetch_status=0
Begin
    Insert into @DS 
    Values (@Ma, (select count(*) from SinhVien where Lop=@Ma))
    Fetch next from cur_L into @Ma
End
Close cur_L
Deallocate cur_L
Return
```

# Constraints

```sql
-- Rule
CREATE RULE ur_LuongDuong
AS @Luong>0
sp_bindRule 'ur_LuongDuong','NhanVien.Luong'
sp_unbindRule 'NhanVien.Luong'

-- Cách 1
CREATE TABLE Persons
(
    P_Id int NOT NULL UNIQUE,
    LastName varchar(255) NOT NULL,
    FirstName varchar(255),
    -- ...
    CONSTRAINT uc_PersonID UNIQUE (LastName, FirstName)
)
-- Cách 2
ALTER TABLE Persons ADD CONSTRAINT Uc_Person UNIQUE (P_id, LastName)

-- Vô hiệu hóa (Check hoặc Foreign) constraint
Alter table <Tab_name> Nocheck constraint {ALL | constraint_name [,…n]}
-- Khởi động (Check hoặc Foreign) constraint đã mất hiệu lực
Alter table <Tab_name> Check constraint {ALL | constraint_name [,…n]}

-- Trigger

Create trigger tên_trigger On {tên_bảng|tên_view} 
-- For | After: Trigger được gọi thực hiện sau thao tác delete/insert/update tương ứng
--- Các dòng mới được thêm chứa đồng thời trong bảng dữ liệu và bảng inserted
--- Các dòng bị xoá chỉ nằm trong bảng deleted
-- Instead of: Trigger được gọi thực hiện thay cho thao tác delete/insert/update tương ứng
--- Các dòng mới được thêm chỉ chứa trong bảng inserted
--- Các dòng bị chỉ định xoá nằm đồng thời trong bảng dữ liệu và bảng deleted
{For | After | Instead of} { [delete] [, insert] [, update] }
As
Begin
    { 
        các lệnh T-sql 
        -- Update(col_name) = true : có thực hiện cập nhật trên cột col_name
    }
End
Go

-- Bước 1: xác định bảng tầm ảnh hưởng
-- Bước 2: với mỗi quan hệ trong bảng tầm ảnh hưởng xác định xử lí trên các thao tác khi vi phạm RBTV
-- Bước 3: các thao tác có cùng xử lí sẽ được viết trong cùng một trigger

-- Ví dụ:
-- DonHang (MaDH,...,NgayDatHang)
-- PhieuGH (MaPG, MaDH,...,NgayGiaoHang)
-- RBTV : Ngày giao hàng phải sau ngày đặt hàng và không trễ quá 1 tháng (30 ngày) kể từ ngày đặt hàng
|Insert|Delete|Update|
|DonHang|-|-|+(NgayDatHang)|
|PhieuGH|+|-|+(MaDH,NgayGiaoHang)|

Create trigger trg_DH_PGH On DonHang
For update
As
Begin
    If exists (
            select * from Inserted I, PhieuGH P
            Where P.MaDH=I.MaDH And (P.NgayGiaoHang < I.NgayDatHang Or Datediff(MM, I.NgayDatHang, P.NgayGiaoHang) > 1)
        )
    Begin
        Raiserror('Ngay dat hang khong hop le', 0, 1)
        Rollback transaction
    End 
End

Create trigger trg_PGH On PhieuGH
For update, insert
As
Begin
    If exists(
            select * from Inserted I, DonHang D
            Where I.MaDH=D.MaDH And (I.NgayGiaoHang < D.NgayDatHang Or Datediff(MM, D.NgayDatHang, I.NgayGiaoHang) > 1)
        )
    Begin
        Raiserror(‘Ngay dat hang khong hop le’,0,1)
        Rollback transaction
    End 
End

-- Nested Trigger
EXEC SP_CONFIGURE 'Nested_Triggers', 0
```

# View

```sql
{Create | Alter} view view_name [(column_name [ ,...n])] 
[With Schemabinding] -- gắn view với schema
As select_statement

CREATE VIEW uv_ThongTinSV
AS
Select *
From SinhVien

-- - Không thể xóa bảng hay view khác có liên quan đến định nghĩa của view có schemabinding
-- - Không thể thay đổi cấu trúc bảng/view nếu việc thay đổi này ảnh hưởng đến định nghĩa view có schemabinding
-- - Câu select định nghĩa view phải chỉ định rõ thuộc tính (không được select * )
-- - Các tên bảng, view trong câu select phải được viết dưới dạng schema.Tênbảng/Tênview
-- - Nếu view schemabinding định nghĩa trên một view khác, view đó cũng phải ở dạng schemabinding

CREATE VIEW uv_ThongTinSV(MaSV, Hoten, MaKH)
With Schemabinding
AS
Select MaSV, HoTen, MaKhoa
From dbo.SinhVien

-- Lệnh select trong định nghĩa view không được chứa:
-- - Order by, nếu có phải kèm với TOP
-- - Select into
-- Không thể gắn kết rule hay default vào view
-- Không thể khai báo trigger for/after trên view (chỉ có thể khai báo trigger instead of)
-- Một view có thể được định nghĩa trên một view khác, nhưng không quá 32 cấp

-- View chỉ có thể được cập nhật nếu:
-- Lệnh select định nghĩa view không chứa:
-- - Các hàm tổng hợp (count, sum, avg, max,…)
-- - Group by, Top, select distinct
-- - Union
-- Các thao tác cập nhật (insert/delete/update) chỉ tham chiếu đến các cột của duy nhất một bảng
-- Không thỏa các điều kiện trên, nhưng có trigger instead of for insert/ update/ delete tương ứng
```

# Transaction

```sql
Create proc TenProc
As
Begin
    Begin tran
        begin try
            -- statments go here
        end try
        begin catch
            rollback tran
        end catch

    commit tran
End

Create proc TenProc
As
Begin
    Begin tran
        -- statments go here
        if @@error <> 0 -- có lỗi
            rollback tran

    commit tran
End
```