class CreatePosts < ActiveRecord::Migration
  def change
    create_table :posts do |t|
      # t.type "name", options - New style or 'sexy' migrations
      t.string "title", :null => false
      t.integer "cover_type", :default => 0 # 0 - Unspecified, 1 - Soft, 2 = Hard
      t.integer "edition"
      t.float "price", :null => false
      t.float "isbn"
      t.timestamps
    end
  end
end
