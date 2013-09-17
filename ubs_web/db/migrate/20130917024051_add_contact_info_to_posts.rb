class AddContactInfoToPosts < ActiveRecord::Migration
  def change
    add_column :posts, :email, :string
    add_column :posts, :phone, :string, limit: 10
  end
end
