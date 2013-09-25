class AddDeviceToPosts < ActiveRecord::Migration
  def change
    add_column :posts, :device, :string, :default => "Web"
  end
end
