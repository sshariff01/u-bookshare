class AlterPosts < ActiveRecord::Migration
  def change
    change_column :posts, :title, :string, :default => ""
    change_column :posts, :edition, :integer, :default => 0
    change_column :posts, :price, :float, :default => 0
    change_column :posts, :isbn, :float, :default => 0
    change_column :posts, :email, :string, :default => "n/a"
    change_column :posts, :phone, :string, :default => "n/a"
  end
end